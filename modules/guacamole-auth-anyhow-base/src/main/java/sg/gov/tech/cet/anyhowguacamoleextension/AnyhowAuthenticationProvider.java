package sg.gov.tech.cet.anyhowguacamoleextension;

import sg.gov.tech.cet.anyhowguacamoleextension.model.guacamole.*;
import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.*;
import sg.gov.tech.cet.anyhowguacamoleextension.model.guacamole.*;
import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.*;
import sg.gov.tech.cet.anyhowguacamoleextension.util.OverrideUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.form.TextField;
import org.apache.guacamole.net.auth.*;
import org.apache.guacamole.net.auth.simple.SimpleDirectory;
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
        List<AnyhowUser> users = configuration.getUsers();

        if (CollectionUtils.isNotEmpty(users)) {
            if (IterableUtils.matchesAny(users, user -> user.validateCredentials(credentials))) {
                AnyhowGuacamoleAuthenticatedUser authenticatedUser = new AnyhowGuacamoleAuthenticatedUser();
                authenticatedUser.setAuthenticationProvider(this);
                authenticatedUser.setCredentials(credentials);
                authenticatedUser.setIdentifier(credentials.getUsername());
                return authenticatedUser;
            }
        }

        return null;
    }


    @Override
    public UserContext getUserContext(AuthenticatedUser authenticatedUser) throws GuacamoleException {
        String username = authenticatedUser.getCredentials().getUsername();
        AnyhowConfiguration configuration = loadConfiguration(authenticatedUser.getCredentials(), environment);

        List<AnyhowConnection> connections = configuration.getConnections();
        List<AnyhowConnectionGroup> connectionGroups = configuration.getConnectionGroups();
        List<AnyhowUser> users = configuration.getUsers();
        List<AnyhowUserGroup> userGroups = configuration.getUserGroups();


        Map<String, AnyhowUser> userMap = new HashMap<>();
        Map<String, AnyhowGuacamoleUser> guacamoleUserMap = new HashMap<>();
        Set<String> userFormFieldNames = new HashSet<>();
        if (CollectionUtils.isNotEmpty(users)) {
            MapUtils.populateMap(userMap, users, AnyhowUser::getIdentifier);
            MapUtils.populateMap(guacamoleUserMap, users, AnyhowUser::getIdentifier, AnyhowGuacamoleUser::new);
            users.forEach(user -> {
                if (MapUtils.isNotEmpty(user.getAttributes())) {
                    userFormFieldNames.addAll(user.getAttributes().keySet());
                }
            });
        }

        if (CollectionUtils.isNotEmpty(users) && !IterableUtils.matchesAny(users, user -> user.getIdentifier().equals(username))) {
            return null;
        }


        Map<String, AnyhowConnection> connectionMap = new HashMap<>();
        Map<String, AnyhowGuacamoleConnection> guacamoleConnectionMap = new HashMap<>();
        Map<String, Set<String>> connectionIdentifiersToAuthorizedUserIdentifiers = new HashMap<>();
        Map<String, Set<String>> authorizedUserIdentifiersToConnectionIdentifiers = new HashMap<>();
        Map<String, Set<String>> connectionIdentifiersToAuthorizedUserGroupIdentifiers = new HashMap<>();
        Map<String, Set<String>> authorizedUserGroupIdentifiersToConnectionIdentifiers = new HashMap<>();
        Set<String> connectionFormFieldNames = new HashSet<>();
        if (CollectionUtils.isNotEmpty(connections)) {
            MapUtils.populateMap(connectionMap, connections, AnyhowConnection::getIdentifier);
            MapUtils.populateMap(guacamoleConnectionMap, connections, AnyhowConnection::getIdentifier, AnyhowGuacamoleConnection::new);

            connectionMap.forEach((connectionIdentifier, connection) -> {
                if (MapUtils.isNotEmpty(connection.getAttributes())) {
                    connectionFormFieldNames.addAll(connection.getAttributes().keySet());
                }

                if (CollectionUtils.isNotEmpty(connection.getAuthorizedUsers())) {
                    Set<String> authorizedUserIdentifiers = connectionIdentifiersToAuthorizedUserIdentifiers.computeIfAbsent(connectionIdentifier, ignored -> new HashSet<>());
                    authorizedUserIdentifiers.addAll(connection.getAuthorizedUsers());

                    for (String authorizedUserIdentifier : connection.getAuthorizedUsers()) {
                        Set<String> connectionIdentifiers = authorizedUserIdentifiersToConnectionIdentifiers.computeIfAbsent(authorizedUserIdentifier, ignored -> new HashSet<>());
                        connectionIdentifiers.add(connectionIdentifier);
                    }
                }

                if (CollectionUtils.isNotEmpty(connection.getAuthorizedUserGroups())) {
                    Set<String> authorizedUserGroupIdentifiers = connectionIdentifiersToAuthorizedUserGroupIdentifiers.computeIfAbsent(connectionIdentifier, ignored -> new HashSet<>());
                    authorizedUserGroupIdentifiers.addAll(connection.getAuthorizedUserGroups());

                    for (String authorizedUserGroupIdentifier : connection.getAuthorizedUserGroups()) {
                        Set<String> connectionIdentifiers = authorizedUserGroupIdentifiersToConnectionIdentifiers.computeIfAbsent(authorizedUserGroupIdentifier, ignored -> new HashSet<>());
                        connectionIdentifiers.add(connectionIdentifier);
                    }
                }
            });

            OverrideUtil.applyOverrides(environment, new ArrayList<>(guacamoleConnectionMap.values()));
        }


        Map<String, AnyhowConnectionGroup> connectionGroupMap = new HashMap<>();
        Map<String, AnyhowGuacamoleConnectionGroup> guacamoleConnectionGroupMap = new HashMap<>();
        Map<String, String> childConnectionIdentifiersToParentConnectionGroupIdentifiers = new HashMap<>();
        Map<String, String> childConnectionGroupIdentifiersToParentConnectionGroupIdentifiers = new HashMap<>();
        Map<String, Set<String>> parentConnectionGroupIdentifiersToChildConnectionIdentifiers = new HashMap<>();
        Map<String, Set<String>> parentConnectionGroupIdentifiersToChildConnectionGroupIdentifiers = new HashMap<>();
        Map<String, Set<String>> authorizedUserIdentifiersToConnectionGroupIdentifiers = new HashMap<>();
        Map<String, Set<String>> authorizedUserGroupIdentifiersToConnectionGroupIdentifiers = new HashMap<>();
        Map<String, Set<String>> connectionGroupIdentifiersToAuthorizedUserIdentifiers = new HashMap<>();
        Map<String, Set<String>> connectionGroupIdentifiersToAuthorizedUserGroupIdentifiers = new HashMap<>();
        Set<String> connectionGroupFormFieldNames = new HashSet<>();
        if (CollectionUtils.isNotEmpty(connectionGroups)) {
            MapUtils.populateMap(connectionGroupMap, connectionGroups, AnyhowConnectionGroup::getIdentifier);
            MapUtils.populateMap(guacamoleConnectionGroupMap, connectionGroups, AnyhowConnectionGroup::getIdentifier, AnyhowGuacamoleConnectionGroup::new);

            connectionGroupMap.forEach((connectionGroupIdentifier, connectionGroup) -> {
                if (MapUtils.isNotEmpty(connectionGroup.getAttributes())) {
                    connectionGroupFormFieldNames.addAll(connectionGroup.getAttributes().keySet());
                }


                if (CollectionUtils.isNotEmpty(connectionGroup.getConnections())) {
                    parentConnectionGroupIdentifiersToChildConnectionIdentifiers.put(connectionGroupIdentifier, new HashSet<>(connectionGroup.getConnections()));
                    for (String childConnectionIdentifier : connectionGroup.getConnections()) {
                        if (!childConnectionIdentifiersToParentConnectionGroupIdentifiers.containsKey(childConnectionIdentifier)) {
                            childConnectionIdentifiersToParentConnectionGroupIdentifiers.put(childConnectionIdentifier, connectionGroupIdentifier);
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(connectionGroup.getConnectionGroups())) {
                    parentConnectionGroupIdentifiersToChildConnectionGroupIdentifiers.put(connectionGroupIdentifier, new HashSet<>(connectionGroup.getConnectionGroups()));
                    for (String childConnectionGroupIdentifier : connectionGroup.getConnectionGroups()) {
                        if (!childConnectionGroupIdentifiersToParentConnectionGroupIdentifiers.containsKey(childConnectionGroupIdentifier)) {
                            childConnectionGroupIdentifiersToParentConnectionGroupIdentifiers.put(childConnectionGroupIdentifier, connectionGroupIdentifier);
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(connectionGroup.getAuthorizedUsers())) {
                    Set<String> authorizedUserIdentifiers = connectionGroupIdentifiersToAuthorizedUserIdentifiers.computeIfAbsent(connectionGroupIdentifier, ignored -> new HashSet<>());
                    authorizedUserIdentifiers.addAll(connectionGroup.getAuthorizedUsers());

                    for (String userIdentifier : connectionGroup.getAuthorizedUsers()) {
                        Set<String> connectionGroupIdentifiers = authorizedUserIdentifiersToConnectionGroupIdentifiers.computeIfAbsent(userIdentifier, ignored -> new HashSet<>());
                        connectionGroupIdentifiers.add(connectionGroupIdentifier);
                    }
                }

                if (CollectionUtils.isNotEmpty(connectionGroup.getAuthorizedUserGroups())) {
                    Set<String> authorizedUserGroupIdentifiers = connectionGroupIdentifiersToAuthorizedUserGroupIdentifiers.computeIfAbsent(connectionGroupIdentifier, ignored -> new HashSet<>());
                    authorizedUserGroupIdentifiers.addAll(connectionGroup.getAuthorizedUserGroups());

                    for (String userGroupIdentifier : connectionGroup.getAuthorizedUsers()) {
                        Set<String> connectionGroupIdentifiers = authorizedUserGroupIdentifiersToConnectionGroupIdentifiers.computeIfAbsent(userGroupIdentifier, ignored -> new HashSet<>());
                        connectionGroupIdentifiers.add(connectionGroupIdentifier);
                    }
                }
            });


            childConnectionGroupIdentifiersToParentConnectionGroupIdentifiers.forEach((childConnectionGroupIdentifier, parentConnectionGroupIdentifier) -> {
                AnyhowGuacamoleConnectionGroup guacamoleConnectionGroup = guacamoleConnectionGroupMap.get(childConnectionGroupIdentifier);
                if (guacamoleConnectionGroup != null) {
                    guacamoleConnectionGroup.setParentIdentifier(parentConnectionGroupIdentifier);
                }
            });

            childConnectionIdentifiersToParentConnectionGroupIdentifiers.forEach((childConnectionIdentifier, parentConnectionGroupIdentifier) -> {
                AnyhowGuacamoleConnection guacamoleConnection = guacamoleConnectionMap.get(childConnectionIdentifier);
                if (guacamoleConnection != null) {
                    guacamoleConnection.setParentIdentifier(parentConnectionGroupIdentifier);
                }
            });


        }

        Map<String, AnyhowUserGroup> userGroupMap = new HashMap<>();
        Map<String, AnyhowGuacamoleUserGroup> guacamoleUserGroupMap = new HashMap<>();


        Map<String, Set<String>> memberUserIdentifiersToMemberOfUserGroupIdentifiers = new HashMap<>();
        Map<String, Set<String>> memberUserGroupIdentifiersToMemberOfUserGroupIdentifiers = new HashMap<>();

        Set<String> userGroupFormFieldNames = new HashSet<>();
        if (CollectionUtils.isNotEmpty(userGroups)) {
            MapUtils.populateMap(userGroupMap, userGroups, AnyhowUserGroup::getIdentifier);
            MapUtils.populateMap(guacamoleUserGroupMap, userGroups, AnyhowUserGroup::getIdentifier, AnyhowGuacamoleUserGroup::new);

            userGroupMap.forEach((userGroupIdentifier, userGroup) -> {
                if (MapUtils.isNotEmpty(userGroup.getAttributes())) {
                    userGroupFormFieldNames.addAll(userGroup.getAttributes().keySet());
                }


                if (CollectionUtils.isNotEmpty(userGroup.getUsers())) {
                    for (String childUserIdentifier : userGroup.getUsers()) {
                        Set<String> memberOfUserGroupIdentifiers = memberUserIdentifiersToMemberOfUserGroupIdentifiers.computeIfAbsent(childUserIdentifier, ignored -> new HashSet<>());
                        memberOfUserGroupIdentifiers.add(userGroupIdentifier);
                    }
                }

                if (CollectionUtils.isNotEmpty(userGroup.getGroups())) {
                    for (String childUserGroupIdentifier : userGroup.getGroups()) {
                        Set<String> memberOfUserGroupIdentifiers = memberUserGroupIdentifiersToMemberOfUserGroupIdentifiers.computeIfAbsent(childUserGroupIdentifier, ignored -> new HashSet<>());
                        memberOfUserGroupIdentifiers.add(userGroupIdentifier);
                    }
                }
            });

            memberUserIdentifiersToMemberOfUserGroupIdentifiers.forEach((childUserIdentifier, parentUserGroupIdentifiers) -> {
                AnyhowGuacamoleUser guacamoleUser = guacamoleUserMap.get(childUserIdentifier);
                if (guacamoleUser != null) {
                    guacamoleUser.setUserGroups(parentUserGroupIdentifiers);
                }
            });

            memberUserGroupIdentifiersToMemberOfUserGroupIdentifiers.forEach((childUserGroupIdentifier, parentUserGroupIdentifiers) -> {
                AnyhowGuacamoleUserGroup guacamoleUserGroup = guacamoleUserGroupMap.get(childUserGroupIdentifier);
                if (guacamoleUserGroup != null) {
                    guacamoleUserGroup.setUserGroups(parentUserGroupIdentifiers);
                }
            });


        }


        // Map all user identifiers to their effective group memberships
        Map<String, Set<String>> userIdentifierToEffectiveMemberOfUserGroupIdentifiers = new HashMap<>();
        memberUserIdentifiersToMemberOfUserGroupIdentifiers.forEach((memberUserIdentifier, memberOfUserGroupIdentifiers) -> {
            Set<String> effectiveMemberOfUserGroupIdentifiers = userIdentifierToEffectiveMemberOfUserGroupIdentifiers.computeIfAbsent(memberUserIdentifier, ignored -> new HashSet<>());
            effectiveMemberOfUserGroupIdentifiers.addAll(memberOfUserGroupIdentifiers);
            Queue<String> memberOfUserGroupIdentifiersQueue = new ArrayDeque<>(memberOfUserGroupIdentifiers);
            while (!memberOfUserGroupIdentifiersQueue.isEmpty()) {
                String memberOfUserGroupIdentifier = memberOfUserGroupIdentifiersQueue.remove();
                Set<String> parentMemberOfUserGroupIdentifiers = memberUserGroupIdentifiersToMemberOfUserGroupIdentifiers.get(memberOfUserGroupIdentifier);
                if (CollectionUtils.isNotEmpty(parentMemberOfUserGroupIdentifiers)) {
                    for (String parentMemberOfUserGroupIdentifier : parentMemberOfUserGroupIdentifiers) {
                        if (!memberOfUserGroupIdentifiersQueue.contains(parentMemberOfUserGroupIdentifier) && !effectiveMemberOfUserGroupIdentifiers.contains(parentMemberOfUserGroupIdentifier)) {
                            memberOfUserGroupIdentifiersQueue.add(parentMemberOfUserGroupIdentifier);
                        }
                    }
                }
                effectiveMemberOfUserGroupIdentifiers.add(memberOfUserGroupIdentifier);
            }
        });


        Map<String, Set<String>> effectiveAuthorizedUserIdentifiersToConnectionIdentifiers = new HashMap<>();
        Map<String, Set<String>> effectiveAuthorizedUserIdentifiersToConnectionGroupIdentifiers = new HashMap<>();

        // Map all user identifiers to directly authorized connections
        authorizedUserIdentifiersToConnectionIdentifiers.forEach((userIdentifier, authorizedConnectionIdentifiers) -> {
            Set<String> effectiveAuthorizedConnectionIdentifiers = effectiveAuthorizedUserIdentifiersToConnectionIdentifiers.computeIfAbsent(userIdentifier, ignored -> new HashSet<>());
            effectiveAuthorizedConnectionIdentifiers.addAll(authorizedConnectionIdentifiers);
        });

        // Map all user identifiers to directly authorized connection groups
        authorizedUserIdentifiersToConnectionGroupIdentifiers.forEach((userIdentifier, authorizedConnectionGroupIdentifiers) -> {
            Set<String> effectiveAuthorizedConnectionGroupIdentifiers = effectiveAuthorizedUserIdentifiersToConnectionGroupIdentifiers.computeIfAbsent(userIdentifier, ignored -> new HashSet<>());
            effectiveAuthorizedConnectionGroupIdentifiers.addAll(authorizedConnectionGroupIdentifiers);
            Queue<String> connectionGroupIdentifiersQueue = new ArrayDeque<>(authorizedConnectionGroupIdentifiers);
            while (!connectionGroupIdentifiersQueue.isEmpty()) {
                String connectionGroupIdentifier = connectionGroupIdentifiersQueue.remove();
                Set<String> childConnectionGroupIdentifiers = parentConnectionGroupIdentifiersToChildConnectionGroupIdentifiers.get(connectionGroupIdentifier);
                if (CollectionUtils.isNotEmpty(childConnectionGroupIdentifiers)) {
                    for (String childConnectionGroupIdentifier : childConnectionGroupIdentifiers) {
                        if (!connectionGroupIdentifiersQueue.contains(childConnectionGroupIdentifier) && !effectiveAuthorizedConnectionGroupIdentifiers.contains(childConnectionGroupIdentifier)) {
                            connectionGroupIdentifiersQueue.add(childConnectionGroupIdentifier);
                        }
                    }
                }
                effectiveAuthorizedConnectionGroupIdentifiers.add(connectionGroupIdentifier);
            }
        });


        // Map all user identifiers to effective authorized connection groups and their children based on effective user group membership
        userIdentifierToEffectiveMemberOfUserGroupIdentifiers.forEach((userIdentifier, effectiveMemberOfUserGroupIdentifiers) -> {
            Set<String> effectiveAuthorizedConnectionGroupIdentifiers = effectiveAuthorizedUserIdentifiersToConnectionGroupIdentifiers.computeIfAbsent(userIdentifier, ignored -> new HashSet<>());
            effectiveMemberOfUserGroupIdentifiers.forEach(effectiveMemberOfUserGroupIdentifier -> {
                Set<String> connectionGroupIdentifiers = authorizedUserGroupIdentifiersToConnectionGroupIdentifiers.get(effectiveMemberOfUserGroupIdentifier);
                if (CollectionUtils.isNotEmpty(connectionGroupIdentifiers)) {
                    effectiveAuthorizedConnectionGroupIdentifiers.addAll(connectionGroupIdentifiers);
                    Queue<String> connectionGroupIdentifiersQueue = new ArrayDeque<>(connectionGroupIdentifiers);
                    while (!connectionGroupIdentifiersQueue.isEmpty()) {
                        String connectionGroupIdentifier = connectionGroupIdentifiersQueue.remove();
                        Set<String> childConnectionGroupIdentifiers = parentConnectionGroupIdentifiersToChildConnectionGroupIdentifiers.get(connectionGroupIdentifier);
                        if (CollectionUtils.isNotEmpty(childConnectionGroupIdentifiers)) {
                            for (String childConnectionGroupIdentifier : childConnectionGroupIdentifiers) {
                                if (!connectionGroupIdentifiersQueue.contains(childConnectionGroupIdentifier) && !effectiveAuthorizedConnectionGroupIdentifiers.contains(childConnectionGroupIdentifier)) {
                                    connectionGroupIdentifiersQueue.add(childConnectionGroupIdentifier);
                                }
                            }
                        }
                        effectiveAuthorizedConnectionGroupIdentifiers.add(connectionGroupIdentifier);
                    }
                }
            });
        });

        // Map all user identifiers to effective authorized connections based on effective authorized connection groups
        effectiveAuthorizedUserIdentifiersToConnectionGroupIdentifiers.forEach((userIdentifier, effectiveAuthorizedConnectionGroupIdentifiers) -> {
            Set<String> effectiveAuthorizedConnectionIdentifiers = effectiveAuthorizedUserIdentifiersToConnectionIdentifiers.computeIfAbsent(userIdentifier, ignored -> new HashSet<>());

            effectiveAuthorizedConnectionGroupIdentifiers.forEach(effectiveAuthorizedConnectionGroupIdentifier -> {
                Set<String> childConnectionIdentifiers = parentConnectionGroupIdentifiersToChildConnectionIdentifiers.get(effectiveAuthorizedConnectionGroupIdentifier);
                if (CollectionUtils.isNotEmpty(childConnectionIdentifiers)) {
                    effectiveAuthorizedConnectionIdentifiers.addAll(childConnectionIdentifiers);
                }
            });

        });


        // Set usergroup permissions
        guacamoleUserGroupMap.forEach((userGroupIdentifier, guacamoleUserGroup) -> {
            Set<String> connectionIdentifiers = authorizedUserGroupIdentifiersToConnectionIdentifiers.get(userGroupIdentifier);
            if (CollectionUtils.isNotEmpty(connectionIdentifiers)) {
                guacamoleUserGroup.setConnectionPermissions(connectionIdentifiers);
            }
            Set<String> connectionGroupIdentifiers = authorizedUserGroupIdentifiersToConnectionGroupIdentifiers.get(userGroupIdentifier);
            if (CollectionUtils.isNotEmpty(connectionGroupIdentifiers)) {
                guacamoleUserGroup.setConnectionGroupPermissions(connectionGroupIdentifiers);
            }

            guacamoleUserGroup.setUserPermissions(guacamoleUserMap.keySet());
            guacamoleUserGroup.setUserGroupPermissions(guacamoleUserGroupMap.keySet());
        });


        // Set user permissions
        guacamoleUserMap.forEach((userIdentifier, guacamoleUser) -> {
            Set<String> connectionIdentifiers = authorizedUserIdentifiersToConnectionIdentifiers.get(userIdentifier);
            if (CollectionUtils.isNotEmpty(connectionIdentifiers)) {
                guacamoleUser.setConnectionPermissions(connectionIdentifiers);
            }
            Set<String> connectionGroupIdentifiers = authorizedUserIdentifiersToConnectionGroupIdentifiers.get(userIdentifier);
            if (CollectionUtils.isNotEmpty(connectionGroupIdentifiers)) {
                guacamoleUser.setConnectionGroupPermissions(connectionGroupIdentifiers);
            }
            Set<String> effectiveConnectionIdentifiers = effectiveAuthorizedUserIdentifiersToConnectionIdentifiers.get(userIdentifier);
            if (CollectionUtils.isNotEmpty(effectiveConnectionIdentifiers)) {
                guacamoleUser.setConnectionPermissions(effectiveConnectionIdentifiers);
            }
            Set<String> effectiveConnectionGroupIdentifiers = effectiveAuthorizedUserIdentifiersToConnectionGroupIdentifiers.get(userIdentifier);
            if (CollectionUtils.isNotEmpty(effectiveConnectionGroupIdentifiers)) {
                guacamoleUser.setConnectionGroupPermissions(effectiveConnectionGroupIdentifiers);
            }

            guacamoleUser.setUserPermissions(guacamoleUserMap.keySet());
            guacamoleUser.setUserGroupPermissions(guacamoleUserGroupMap.keySet());

            guacamoleUser.setEffectiveUserPermissions(guacamoleUserMap.keySet());
            guacamoleUser.setEffectiveUserGroupPermissions(guacamoleUserGroupMap.keySet());
        });


        AnyhowGuacamoleUser self = guacamoleUserMap.get(username);
        // set self user fields
        if (self == null) {

            self = new AnyhowGuacamoleUser();

            if (MapUtils.isEmpty(authorizedUserIdentifiersToConnectionIdentifiers)) {
                self.setConnectionPermissions(guacamoleConnectionMap.keySet());
                self.setEffectiveConnectionPermissions(guacamoleConnectionMap.keySet());
            } else {
                self.setConnectionPermissions(authorizedUserIdentifiersToConnectionIdentifiers.getOrDefault(username, Collections.emptySet()));
                self.setEffectiveConnectionPermissions(effectiveAuthorizedUserIdentifiersToConnectionIdentifiers.getOrDefault(username, Collections.emptySet()));
            }

            if (MapUtils.isEmpty(authorizedUserIdentifiersToConnectionGroupIdentifiers)) {
                self.setConnectionGroupPermissions(guacamoleConnectionGroupMap.keySet());
            } else {
                self.setConnectionGroupPermissions(authorizedUserIdentifiersToConnectionGroupIdentifiers.getOrDefault(username, Collections.emptySet()));
                self.setEffectiveConnectionGroupPermissions(effectiveAuthorizedUserIdentifiersToConnectionGroupIdentifiers.getOrDefault(username, Collections.emptySet()));
            }

            self.setUserPermissions(guacamoleUserMap.keySet());
            self.setUserGroupPermissions(guacamoleUserGroupMap.keySet());

            self.setEffectiveUserPermissions(guacamoleUserMap.keySet());
            self.setEffectiveUserGroupPermissions(guacamoleUserGroupMap.keySet());
        }


        AnyhowGuacamoleUserContext guacamoleUserContext = new AnyhowGuacamoleUserContext();
        guacamoleUserContext.setAuthenticationProvider(this);
        guacamoleUserContext.setSelf(self);


        if (MapUtils.isNotEmpty(guacamoleConnectionMap)) {
            Directory<Connection> connectionDirectory = new SimpleDirectory<>(new HashMap<>(guacamoleConnectionMap));
            guacamoleUserContext.setConnectionDirectory(connectionDirectory);
        }


        if (MapUtils.isNotEmpty(guacamoleConnectionGroupMap)) {
            Directory<ConnectionGroup> connectionGroupDirectory = new SimpleDirectory<>(new HashMap<>(guacamoleConnectionGroupMap));
            guacamoleUserContext.setConnectionGroupDirectory(connectionGroupDirectory);
        }


        if (MapUtils.isNotEmpty(guacamoleUserMap)) {
            Directory<User> userDirectory = new SimpleDirectory<>(new HashMap<>(guacamoleUserMap));
            guacamoleUserContext.setUserDirectory(userDirectory);
        }

        if (MapUtils.isNotEmpty(guacamoleUserGroupMap)) {
            Directory<UserGroup> userGroupDirectory = new SimpleDirectory<>(new HashMap<>(guacamoleUserGroupMap));
            guacamoleUserContext.setUserGroupDirectory(userGroupDirectory);
        }

        if (CollectionUtils.isNotEmpty(connectionFormFieldNames)) {
            Form connectionAttributesForm = new Form("connection-attributes", CollectionUtils.collect(connectionFormFieldNames, TextField::new));
            guacamoleUserContext.setConnectionAttributes(Collections.singleton(connectionAttributesForm));
        }

        if (CollectionUtils.isNotEmpty(connectionGroupFormFieldNames)) {
            Form connectionGroupAttributesForm = new Form("connection-group-attributes", CollectionUtils.collect(connectionGroupFormFieldNames, TextField::new));
            guacamoleUserContext.setConnectionGroupAttributes(Collections.singleton(connectionGroupAttributesForm));
        }

        if (CollectionUtils.isNotEmpty(userFormFieldNames)) {
            Form userAttributesForm = new Form("user-attributes", CollectionUtils.collect(userFormFieldNames, TextField::new));
            guacamoleUserContext.setUserAttributes(Collections.singleton(userAttributesForm));
        }

        if (CollectionUtils.isNotEmpty(userGroupFormFieldNames)) {
            Form userGroupAttributesForm = new Form("user-group-attributes", CollectionUtils.collect(userGroupFormFieldNames, TextField::new));
            guacamoleUserContext.setUserGroupAttributes(Collections.singleton(userGroupAttributesForm));
        }

        // Set root connection group

        AnyhowGuacamoleConnectionGroup rootGuacamoleConnectionGroup = new AnyhowGuacamoleConnectionGroup();
        Set<String> rootChildConnectionIdentifiers = SetUtils.difference(connectionMap.keySet(), childConnectionIdentifiersToParentConnectionGroupIdentifiers.keySet());
        if (CollectionUtils.isNotEmpty(rootChildConnectionIdentifiers)) {
            rootChildConnectionIdentifiers.forEach(rootChildConnectionIdentifier -> {
                AnyhowGuacamoleConnection guacamoleConnection = guacamoleConnectionMap.get(rootChildConnectionIdentifier);
                if (guacamoleConnection != null) {
                    guacamoleConnection.setParentIdentifier("ROOT");
                }
            });
        }
        Set<String> rootChildConnectionGroupIdentifiers = SetUtils.difference(connectionGroupMap.keySet(), childConnectionGroupIdentifiersToParentConnectionGroupIdentifiers.keySet());
        if (CollectionUtils.isNotEmpty(rootChildConnectionGroupIdentifiers)) {
            rootChildConnectionGroupIdentifiers.forEach(rootChildConnectionGroupIdentifier -> {
                AnyhowGuacamoleConnectionGroup guacamoleConnectionGroup = guacamoleConnectionGroupMap.get(rootChildConnectionGroupIdentifier);
                if (guacamoleConnectionGroup != null) {
                    guacamoleConnectionGroup.setParentIdentifier("ROOT");
                }
            });
        }
        rootGuacamoleConnectionGroup.setConnectionGroupIdentifiers(rootChildConnectionGroupIdentifiers);
        rootGuacamoleConnectionGroup.setConnectionIdentifiers(rootChildConnectionIdentifiers);
        rootGuacamoleConnectionGroup.setParentIdentifier("ROOT");
        rootGuacamoleConnectionGroup.setIdentifier("ROOT");


        guacamoleUserContext.setRootConnectionGroup(rootGuacamoleConnectionGroup);

        return guacamoleUserContext;

    }

}
