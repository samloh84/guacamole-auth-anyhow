package dev.samloh.guacamole_extensions.anyhow.model.jackson;

import java.util.Collections;
import java.util.List;

public class AnyhowConfiguration {

    private List<AnyhowConnection> connections = Collections.emptyList();
    private List<AnyhowConnectionGroup> connectionGroups = Collections.emptyList();
    private List<AnyhowUser> users = Collections.emptyList();
    private List<AnyhowUserGroup> userGroups = Collections.emptyList();

    public List<AnyhowConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<AnyhowConnection> connections) {
        this.connections = connections;
    }

    public List<AnyhowConnectionGroup> getConnectionGroups() {
        return connectionGroups;
    }

    public void setConnectionGroups(List<AnyhowConnectionGroup> connectionGroups) {
        this.connectionGroups = connectionGroups;
    }

    public List<AnyhowUser> getUsers() {
        return users;
    }

    public void setUsers(List<AnyhowUser> users) {
        this.users = users;
    }

    public List<AnyhowUserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<AnyhowUserGroup> userGroups) {
        this.userGroups = userGroups;
    }
}
