package dev.samloh.guacamole_extensions.anyhow;

import dev.samloh.guacamole_extensions.anyhow.model.AnyhowGuacamoleConfiguration;
import dev.samloh.guacamole_extensions.anyhow.model.AnyhowGuacamoleConfigurations;
import dev.samloh.guacamole_extensions.anyhow.util.AnyhowGuacamoleConfigurationsApplyOverrides;
import dev.samloh.guacamole_extensions.anyhow.util.AnyhowGuacamoleExecutableConfiguration;
import dev.samloh.guacamole_extensions.anyhow.util.AnyhowGuacamoleFileConfiguration;
import dev.samloh.guacamole_extensions.anyhow.util.AnyhowGuacamoleUrlConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.simple.SimpleAuthenticationProvider;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnyhowAuthenticationProvider extends SimpleAuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(AnyhowAuthenticationProvider.class);

    @Override
    public String getIdentifier() {
        return "anyhow";
    }


    @Override
    public Map<String, GuacamoleConfiguration> getAuthorizedConfigurations(Credentials credentials) throws GuacamoleException {

        Environment environment = new LocalEnvironment();

        AnyhowGuacamoleConfigurations configurations = null;

        String anyhowUrl = environment.getProperty(
                AnyhowAuthenticationProperties.ANYHOW_URL
        );
        String anyhowExecutablePath = environment.getProperty(
                AnyhowAuthenticationProperties.ANYHOW_EXECUTABLE_PATH
        );
        String anyhowFilePath = environment.getProperty(
                AnyhowAuthenticationProperties.ANYHOW_FILE_PATH
        );

        if (!StringUtils.isBlank(anyhowUrl)) {

            try {
                configurations = AnyhowGuacamoleUrlConfiguration.getConfigurations(environment, credentials);
            } catch (Exception e) {
                String errorMessage = String.format("Could not retrieve Guacamole Configurations from URL: %s", anyhowUrl);
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        } else if (!StringUtils.isBlank(anyhowExecutablePath)) {
            try {
                configurations = AnyhowGuacamoleExecutableConfiguration.getConfigurations(environment, credentials);

            } catch (Exception e) {
                String errorMessage = String.format("Could not retrieve Guacamole Configurations from Executable: %s", anyhowExecutablePath);
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }


        } else if (!StringUtils.isBlank(anyhowFilePath)) {
            try {
                configurations = AnyhowGuacamoleFileConfiguration.getConfigurations(environment);
            } catch (Exception e) {
                String errorMessage = String.format("Could not retrieve Guacamole Configurations from File Path: %s", anyhowFilePath);
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        }

        if (configurations != null) {
            Map<String, GuacamoleConfiguration> guacamoleConfigurationMap = new HashMap<>();
            for (AnyhowGuacamoleConfiguration configuration : configurations.getConfigurations()) {
                GuacamoleConfiguration guacamoleConfiguration = configuration.asGuacamoleConfiguration();
                guacamoleConfigurationMap.put(guacamoleConfiguration.getConnectionID(), guacamoleConfiguration);
            }

            if (!guacamoleConfigurationMap.isEmpty()) {
                AnyhowGuacamoleConfigurationsApplyOverrides.applyOverrides(environment, guacamoleConfigurationMap);
                return guacamoleConfigurationMap;
            }
        }


        return null;
    }

}
