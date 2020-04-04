package dev.samloh.guacamole_extensions.anyhow;

import dev.samloh.guacamole_extensions.anyhow.model.guacamole.*;
import dev.samloh.guacamole_extensions.anyhow.model.jackson.*;
import dev.samloh.guacamole_extensions.anyhow.util.OverrideUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.net.auth.AbstractAuthenticationProvider;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AnyhowAuthenticationProvider extends AbstractAuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(AnyhowAuthenticationProvider.class);

    protected Environment environment;

    public AnyhowAuthenticationProvider() throws GuacamoleException {
        environment = new LocalEnvironment();
    }

    @Override
    public String getIdentifier() {
        return "anyhow";
    }

    public abstract AnyhowConfiguration loadConfiguration(Credentials credentials, Environment environment) throws GuacamoleException;

    @Override
    public AuthenticatedUser authenticateUser(Credentials credentials) throws GuacamoleException {
        AnyhowConfiguration configuration = loadConfiguration(credentials, environment);
        if (configuration.getUsers() != null) {
            for (AnyhowUser user : configuration.getUsers()) {
                if (user.validateCredentials(credentials)) {
                    return new AnyhowGuacamoleAuthenticatedUser(this, credentials, new AnyhowGuacamoleUser(user));
                }
            }
        }

        return null;
    }


    @Override
    public UserContext getUserContext(AuthenticatedUser authenticatedUser) throws GuacamoleException {
        String username = authenticatedUser.getCredentials().getUsername();
        AnyhowConfiguration configuration = loadConfiguration(authenticatedUser.getCredentials(), environment);

        AnyhowGuacamoleUserContext userContext = new AnyhowGuacamoleUserContext();
        userContext.setAuthenticationProvider(this);
        userContext.setAuthenticatedUser(authenticatedUser);


        Map<String, AnyhowGuacamoleConnection> connections = new HashMap<>();
        Map<String, AnyhowGuacamoleConnectionGroup> connectionGroups = new HashMap<>();
        Map<String, AnyhowGuacamoleUserGroup> userGroups = new HashMap<>();
        List<String> groupMembership = null;


        if (configuration.getUsers() != null) {
            for (AnyhowUser anyhowUser : configuration.getUsers()) {
                if (anyhowUser.getIdentifier().equals(username)) {
                    userContext.setUser(new AnyhowGuacamoleUser(anyhowUser));
                    break;
                }
            }
        }

        if (configuration.getUserGroups() != null) {

            groupMembership = new ArrayList<>();

            Queue<String> additionalGroups = new ArrayDeque<>();
            for (AnyhowUserGroup userGroup : configuration.getUserGroups()) {
                if (userGroup.getUsers().contains(username)) {
                    additionalGroups.add(userGroup.getIdentifier());
                }
            }

            while (!additionalGroups.isEmpty()) {
                String groupIdentifier = additionalGroups.remove();
                groupMembership.add(groupIdentifier);
                for (AnyhowUserGroup userGroup : configuration.getUserGroups()) {
                    if (userGroup.getGroups().contains(groupIdentifier)) {
                        if (!groupMembership.contains(userGroup.getIdentifier()) && !additionalGroups.contains(userGroup.getIdentifier())) {
                            additionalGroups.add(userGroup.getIdentifier());
                        }
                    }
                }
            }

            for (AnyhowUserGroup userGroup : configuration.getUserGroups()) {
                if (groupMembership.contains(userGroup.getIdentifier())) {
                    userGroups.put(userGroup.getIdentifier(), new AnyhowGuacamoleUserGroup(userGroup));
                }
            }
        }


        if (configuration.getConnections() != null) {
            for (AnyhowConnection connection : configuration.getConnections()) {
                if (connection.getAuthorizedUsers() != null) {
                    if (!connection.getAuthorizedUsers().contains(username)) {
                        continue;
                    }
                }
                if (connection.getAuthorizedUserGroups() != null) {
                    if (groupMembership == null) {
                        continue;
                    }

                    if (!CollectionUtils.containsAny(connection.getAuthorizedUserGroups(), groupMembership)) {
                        continue;
                    }
                }


                AnyhowGuacamoleConnection guacamoleConnection = new AnyhowGuacamoleConnection(connection);
                guacamoleConnection.setParentIdentifier("ROOT");
                connections.put(connection.getIdentifier(), guacamoleConnection);
            }
        }

        if (configuration.getConnectionGroups() != null) {
            for (AnyhowConnectionGroup connectionGroup : configuration.getConnectionGroups()) {
                if (connectionGroup.getAuthorizedUsers() != null) {
                    if (!connectionGroup.getAuthorizedUsers().contains(username)) {
                        continue;
                    }
                }
                if (connectionGroup.getAuthorizedUserGroups() != null) {
                    if (groupMembership == null) {
                        continue;
                    }

                    if (!CollectionUtils.containsAny(connectionGroup.getAuthorizedUserGroups(), groupMembership)) {
                        continue;
                    }
                }

                AnyhowGuacamoleConnectionGroup guacamoleConnectionGroup = new AnyhowGuacamoleConnectionGroup(connectionGroup);
                guacamoleConnectionGroup.setParentIdentifier("ROOT");
                connectionGroups.put(connectionGroup.getIdentifier(), guacamoleConnectionGroup);
            }

            for (AnyhowConnectionGroup connectionGroup : configuration.getConnectionGroups()) {
                String connectionGroupIdentifier = connectionGroup.getIdentifier();
                for (String subConnectionGroupIdentifier : connectionGroup.getConnectionGroups()) {
                    AnyhowGuacamoleConnectionGroup subConnectionGroup = connectionGroups.get(subConnectionGroupIdentifier);
                    if (subConnectionGroup != null) {
                        subConnectionGroup.setParentIdentifier(connectionGroupIdentifier);
                    }
                }
                for (String subConnectionIdentifier : connectionGroup.getConnections()) {
                    AnyhowGuacamoleConnection subConnection = connections.get(subConnectionIdentifier);
                    if (subConnection != null) {
                        subConnection.setParentIdentifier(connectionGroupIdentifier);
                    }
                }
            }
        }


        if (!connections.isEmpty()) {
            OverrideUtil.applyOverrides(environment, connections.values());
            userContext.setConnections(new ArrayList<>(connections.values()));
        }
        if (!connectionGroups.isEmpty()) {
            userContext.setConnectionGroups(new ArrayList<>(connectionGroups.values()));
        }

        if (groupMembership != null) {
            userContext.setUserGroups(new ArrayList<>(userGroups.values()));
        }


        return userContext;


    }

}
