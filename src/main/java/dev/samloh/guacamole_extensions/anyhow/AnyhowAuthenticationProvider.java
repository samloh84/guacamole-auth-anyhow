package dev.samloh.guacamole_extensions.anyhow;

import dev.samloh.guacamole_extensions.anyhow.model.Configurations;
import dev.samloh.guacamole_extensions.anyhow.util.OverrideUtil;
import dev.samloh.guacamole_extensions.anyhow.util.ExecutableUtil;
import dev.samloh.guacamole_extensions.anyhow.util.FileUtil;
import dev.samloh.guacamole_extensions.anyhow.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.net.auth.*;
import org.apache.guacamole.net.auth.simple.SimpleUserContext;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AnyhowAuthenticationProvider extends AbstractAuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(AnyhowAuthenticationProvider.class);

    private Environment environment;

    public AnyhowAuthenticationProvider() throws GuacamoleException {
        environment = new LocalEnvironment();
    }

    @Override
    public String getIdentifier() {
        return "anyhow";
    }


    public Map<String, GuacamoleConfiguration> getAuthorizedConfigurations(Credentials credentials) throws GuacamoleException {

        Configurations configurations = null;

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
                configurations = UrlUtil.getConfigurations(environment, credentials);
            } catch (Exception e) {
                String errorMessage = String.format("Could not retrieve Guacamole Configurations from URL: %s", anyhowUrl);
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        } else if (!StringUtils.isBlank(anyhowExecutablePath)) {
            try {
                configurations = ExecutableUtil.getConfigurations(environment, credentials);

            } catch (Exception e) {
                String errorMessage = String.format("Could not retrieve Guacamole Configurations from Executable: %s", anyhowExecutablePath);
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }


        } else if (!StringUtils.isBlank(anyhowFilePath)) {
            try {
                configurations = FileUtil.getConfigurations(environment);
            } catch (Exception e) {
                String errorMessage = String.format("Could not retrieve Guacamole Configurations from File Path: %s", anyhowFilePath);
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        }

        if (configurations != null) {
            Map<String, GuacamoleConfiguration> guacamoleConfigurationMap = configurations.asGuacamoleConfigurations();
            if (!guacamoleConfigurationMap.isEmpty()) {
                OverrideUtil.applyOverrides(environment, guacamoleConfigurationMap);
                return guacamoleConfigurationMap;
            }
        }


        return null;
    }


    @Override
    public AuthenticatedUser authenticateUser(Credentials credentials) throws GuacamoleException {
        Boolean alwaysAuthenticate = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_ALWAYS_AUTHENTICATE);
        logger.info(String.format("ANYHOW_ALWAYS_AUTHENTICATE: %s", alwaysAuthenticate));

        if (alwaysAuthenticate) {
            org.apache.guacamole.net.auth.AuthenticationProvider authenticationProvider = this;
            return new AbstractAuthenticatedUser() {

                @Override
                public org.apache.guacamole.net.auth.AuthenticationProvider getAuthenticationProvider() {
                    return authenticationProvider;
                }

                @Override
                public Credentials getCredentials() {
                    return credentials;
                }
            };
        }

        return null;
    }

    @Override
    public UserContext getUserContext(AuthenticatedUser authenticatedUser) throws GuacamoleException {
        return new SimpleUserContext(this, getAuthorizedConfigurations(authenticatedUser.getCredentials()));
    }

}
