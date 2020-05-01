package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowConnectionGroup;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleSecurityException;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.auth.AbstractConnectionGroup;
import org.apache.guacamole.protocol.GuacamoleClientInformation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnyhowGuacamoleConnectionGroup extends AbstractConnectionGroup {
    private Set<String> connectionIdentifiers = new HashSet<>();
    private Set<String> connectionGroupIdentifiers = new HashSet<>();
    private Map<String, String> attributes = new HashMap<>();

    public AnyhowGuacamoleConnectionGroup() {
        this.setType(Type.ORGANIZATIONAL);
    }

    public AnyhowGuacamoleConnectionGroup(AnyhowConnectionGroup connectionGroup) {
        this();
        this.setName(connectionGroup.getName());
        this.setConnectionIdentifiers(new HashSet<>(connectionGroup.getConnections()));
        this.setConnectionGroupIdentifiers(new HashSet<>(connectionGroup.getConnectionGroups()));
        this.setAttributes(new HashMap<>(connectionGroup.getAttributes()));
        this.setIdentifier(connectionGroup.getIdentifier());
    }


    @Override
    public Set<String> getConnectionIdentifiers() throws GuacamoleException {
        return this.connectionIdentifiers;
    }

    @Override
    public Set<String> getConnectionGroupIdentifiers() throws GuacamoleException {
        return this.connectionGroupIdentifiers;
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void setConnectionIdentifiers(Set<String> connectionIdentifiers) {
        this.connectionIdentifiers = connectionIdentifiers;
    }

    public void setConnectionGroupIdentifiers(Set<String> connectionGroupIdentifiers) {
        this.connectionGroupIdentifiers = connectionGroupIdentifiers;
    }

    @Override
    public GuacamoleTunnel connect(GuacamoleClientInformation guacamoleClientInformation) throws GuacamoleException {
        throw new GuacamoleSecurityException("Permission denied.");
    }

    @Override
    public int getActiveConnections() {
        return 0;
    }
}
