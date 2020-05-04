package sg.gov.tech.cet.anyhowguacamoleextension.model.jackson;

import org.apache.commons.collections4.MapUtils;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnyhowConnection {

    private String name;
    private String identifier;
    private Map<String, String> attributes = Collections.emptyMap();

    private String protocol;
    private Map<String, String> parameters = Collections.emptyMap();

    private List<String> authorizedUsers = Collections.emptyList();
    private List<String> authorizedUserGroups = Collections.emptyList();

    public static Map<String, GuacamoleConfiguration> toGuacamoleConfigurations(List<AnyhowConnection> connections) {
        Map<String, GuacamoleConfiguration> configurations = new HashMap<>();
        MapUtils.populateMap(configurations, connections, AnyhowConnection::getIdentifier, AnyhowConnection::toGuacamoleConfiguration);
        return configurations;
    }

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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
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

    public GuacamoleConfiguration toGuacamoleConfiguration() {
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol(protocol);
        configuration.setParameters(parameters);
        return configuration;
    }


}
