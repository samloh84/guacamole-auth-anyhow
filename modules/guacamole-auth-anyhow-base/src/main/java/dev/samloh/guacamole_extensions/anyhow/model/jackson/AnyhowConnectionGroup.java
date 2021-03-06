package dev.samloh.guacamole_extensions.anyhow.model.jackson;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnyhowConnectionGroup {
    private String name;
    private String identifier;
    private Map<String, String> attributes = Collections.emptyMap();

    private List<String> connectionGroups = Collections.emptyList();
    private List<String> connections = Collections.emptyList();

    private List<String> authorizedUsers = Collections.emptyList();
    private List<String> authorizedUserGroups = Collections.emptyList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getConnectionGroups() {
        return connectionGroups;
    }

    public void setConnectionGroups(List<String> connectionGroups) {
        this.connectionGroups = connectionGroups;
    }

    public List<String> getConnections() {
        return connections;
    }

    public void setConnections(List<String> connections) {
        this.connections = connections;
    }

    public List<String> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void setAuthorizedUsers(List<String> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    public List<String> getAuthorizedUserGroups() {
        return authorizedUserGroups;
    }

    public void setAuthorizedUserGroups(List<String> authorizedUserGroups) {
        this.authorizedUserGroups = authorizedUserGroups;
    }
}
