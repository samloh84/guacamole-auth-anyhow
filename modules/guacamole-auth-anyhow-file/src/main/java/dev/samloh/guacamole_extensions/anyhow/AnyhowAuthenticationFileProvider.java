package dev.samloh.guacamole_extensions.anyhow;

import dev.samloh.guacamole_extensions.anyhow.model.guacamole.AnyhowGuacamoleAuthenticatedUser;
import dev.samloh.guacamole_extensions.anyhow.model.guacamole.AnyhowGuacamoleUser;
import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowConfiguration;
import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowUser;
import dev.samloh.guacamole_extensions.anyhow.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyhowAuthenticationFileProvider extends AnyhowAuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(AnyhowAuthenticationFileProvider.class);

    public AnyhowAuthenticationFileProvider() throws GuacamoleException {
        super();
    }

    @Override
    public String getIdentifier() {
        return "anyhow-file";
    }


    @Override
    public AuthenticatedUser authenticateUser(Credentials credentials) throws GuacamoleException {

        Boolean alwaysAuthenticate = environment.getProperty(
                AnyhowAuthenticationFileProperties.ANYHOW_FILE_ALWAYS_AUTHENTICATE, false
        );

        if (alwaysAuthenticate) {
            AnyhowGuacamoleAuthenticatedUser authenticatedUser = new AnyhowGuacamoleAuthenticatedUser();
            authenticatedUser.setIdentifier(credentials.getUsername());
            authenticatedUser.setAuthenticationProvider(this);
            authenticatedUser.setCredentials(credentials);
            return authenticatedUser;
        }

        return super.authenticateUser(credentials);
    }

    @Override
    public AnyhowConfiguration loadConfiguration(Credentials credentials, Environment environment) throws GuacamoleException {
        String anyhowFilePath = environment.getProperty(
                AnyhowAuthenticationFileProperties.ANYHOW_FILE_PATH
        );

        if (!StringUtils.isBlank(anyhowFilePath)) {
            try {
                return FileUtil.getConfigurations(environment);
            } catch (Exception e) {
                String errorMessage = String.format("Could not retrieve Guacamole Configurations from File: %s", anyhowFilePath);
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        }

        return null;
    }

}
