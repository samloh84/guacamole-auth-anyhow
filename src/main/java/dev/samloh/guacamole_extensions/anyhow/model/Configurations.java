package dev.samloh.guacamole_extensions.anyhow.model;

import org.apache.guacamole.protocol.GuacamoleConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Configurations {
    private Map<String, Configuration> configurations;

    public Map<String, Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Map<String, Configuration> configurations) {
        this.configurations = configurations;
    }

    public Map<String, GuacamoleConfiguration> asGuacamoleConfigurations() {
        Map<String, GuacamoleConfiguration> guacamoleConfigurations = new HashMap<>();
        configurations.forEach((key, configuration) -> {
            GuacamoleConfiguration guacamoleConfiguration = configuration.asGuacamoleConfiguration();
            guacamoleConfiguration.setConnectionID(key);
            guacamoleConfigurations.put(key, guacamoleConfiguration);
        });
        return guacamoleConfigurations;
    }
}
