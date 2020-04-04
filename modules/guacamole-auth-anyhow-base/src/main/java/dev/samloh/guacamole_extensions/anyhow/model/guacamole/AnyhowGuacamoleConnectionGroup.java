package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowConnectionGroup;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleSecurityException;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.auth.AbstractConnectionGroup;
import org.apache.guacamole.protocol.GuacamoleClientInformation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnyhowGuacamoleConnectionGroup extends AbstractConnectionGroup {
    private AnyhowConnectionGroup connectionGroup;


    public AnyhowGuacamoleConnectionGroup(AnyhowConnectionGroup connectionGroup) {
        this.connectionGroup = connectionGroup;
    }

    public AnyhowConnectionGroup getConnectionGroup() {
        return connectionGroup;
    }

    public void setConnectionGroup(AnyhowConnectionGroup connectionGroup) {
        this.connectionGroup = connectionGroup;
    }

    @Override
    public String getIdentifier() {
        return connectionGroup.getIdentifier();
    }

    @Override
    public void setIdentifier(String identifier) {
        connectionGroup.setIdentifier(identifier);
    }

    @Override
    public String getName() {
        return connectionGroup.getName();
    }

    @Override
    public void setName(String name) {
        connectionGroup.setName(name);
    }

    @Override
    public Map<String, String> getAttributes() {
        return connectionGroup.getAttributes();
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        connectionGroup.setAttributes(attributes);
    }

    @Override
    public Set<String> getConnectionIdentifiers() {
        return new HashSet<>(connectionGroup.getConnections());
    }

    public void setConnectionIdentifiers(Set<String> connectionIdentifiers) {
        connectionGroup.setConnections(new ArrayList<>(connectionIdentifiers));
    }

    @Override
    public Set<String> getConnectionGroupIdentifiers() {
        return new HashSet<>(connectionGroup.getConnectionGroups());
    }

    public void setConnectionGroupIdentifiers(Set<String> connectionGroupIdentifiers) {
        connectionGroup.setConnectionGroups(new ArrayList<>(connectionGroupIdentifiers));
    }

    @Override
    public GuacamoleTunnel connect(GuacamoleClientInformation info) throws GuacamoleException {
        throw new GuacamoleSecurityException("Permission denied.");
    }

    @Override
    public int getActiveConnections() {
        return 0;
    }


}
