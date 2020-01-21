package dev.samloh.guacamole_extensions.anyhow.model;

import org.apache.guacamole.protocol.GuacamoleConfiguration;

import java.util.Map;

public class AnyhowGuacamoleConfiguration {

    private String connectionId;
    private String protocol;
    private Map<String, String> parameters;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public GuacamoleConfiguration asGuacamoleConfiguration() {
        GuacamoleConfiguration guacamoleConfiguration = new GuacamoleConfiguration();
        guacamoleConfiguration.setConnectionID(this.getConnectionId());
        guacamoleConfiguration.setParameters(this.parameters);
        guacamoleConfiguration.setProtocol(getProtocol());

        return guacamoleConfiguration;
    }
}
