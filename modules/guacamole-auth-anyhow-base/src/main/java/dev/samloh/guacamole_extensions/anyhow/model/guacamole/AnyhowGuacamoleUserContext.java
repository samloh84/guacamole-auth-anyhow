package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import dev.samloh.guacamole_extensions.anyhow.AnyhowAuthenticationProvider;
import org.apache.commons.collections4.MapUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.*;
import org.apache.guacamole.net.auth.permission.ObjectPermissionSet;
import org.apache.guacamole.net.auth.simple.SimpleDirectory;
import org.apache.guacamole.net.auth.simple.SimpleObjectPermissionSet;
import org.apache.guacamole.net.auth.simple.SimpleUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnyhowGuacamoleUserContext extends AbstractUserContext {

    private AnyhowAuthenticationProvider authenticationProvider;
    private AuthenticatedUser authenticatedUser;
    private AnyhowGuacamoleUser user;
    private List<AnyhowGuacamoleUserGroup> userGroups;
    private List<AnyhowGuacamoleConnection> connections;
    private List<AnyhowGuacamoleConnectionGroup> connectionGroups;

    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public AnyhowGuacamoleUser getUser() {
        return user;
    }

    public void setUser(AnyhowGuacamoleUser user) {
        this.user = user;
    }

    public List<AnyhowGuacamoleUserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<AnyhowGuacamoleUserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public List<AnyhowGuacamoleConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<AnyhowGuacamoleConnection> connections) {
        this.connections = connections;
    }

    public List<AnyhowGuacamoleConnectionGroup> getConnectionGroups() {
        return connectionGroups;
    }

    public void setConnectionGroups(List<AnyhowGuacamoleConnectionGroup> connectionGroups) {
        this.connectionGroups = connectionGroups;
    }

    @Override
    public User self() {
        return new SimpleUser(authenticatedUser.getCredentials().getUsername()) {

            @Override
            public ObjectPermissionSet getConnectionGroupPermissions() throws GuacamoleException {
                return new SimpleObjectPermissionSet(getConnectionDirectory().getIdentifiers());
            }

            @Override
            public ObjectPermissionSet getConnectionPermissions() throws GuacamoleException {
                return new SimpleObjectPermissionSet(getConnectionGroupDirectory().getIdentifiers());
            }

        };
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(AnyhowAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public Directory<Connection> getConnectionDirectory() throws GuacamoleException {
        if (connections != null){
            Map<String, Connection> connectionMap = new HashMap<>();
            MapUtils.populateMap(connectionMap, connections, Connection::getIdentifier, connection -> connection);
            return new SimpleDirectory<Connection>(connectionMap);
        }
        return super.getConnectionDirectory();
    }

    @Override
    public Directory<ConnectionGroup> getConnectionGroupDirectory() throws GuacamoleException {
        if (connectionGroups != null){
            Map<String, ConnectionGroup> connectionGroupMap = new HashMap<>();
            MapUtils.populateMap(connectionGroupMap, connectionGroups, ConnectionGroup::getIdentifier, connectionGroup -> connectionGroup);
            return new SimpleDirectory<ConnectionGroup>(connectionGroupMap);
        }

        return super.getConnectionGroupDirectory();
    }


}
