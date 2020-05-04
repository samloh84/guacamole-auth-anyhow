package sg.gov.tech.cet.anyhowguacamoleextension;

import sg.gov.tech.cet.anyhowguacamoleextension.model.guacamole.AnyhowGuacamoleAuthenticatedUser;
import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.AnyhowConfiguration;
import sg.gov.tech.cet.anyhowguacamoleextension.util.ExecutableUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyhowAuthenticationExecutableProvider extends AnyhowAuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(AnyhowAuthenticationExecutableProvider.class);

    public AnyhowAuthenticationExecutableProvider() throws GuacamoleException {
        super();
    }

    @Override
    public String getIdentifier() {
        return "anyhow-executable";
    }


    @Override
    public AuthenticatedUser authenticateUser(Credentials credentials) throws GuacamoleException {

        Boolean alwaysAuthenticate = environment.getProperty(
                AnyhowAuthenticationExecutableProperties.ANYHOW_EXECUTABLE_ALWAYS_AUTHENTICATE, false
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
        String anyhowExecutablePath = environment.getProperty(
                AnyhowAuthenticationExecutableProperties.ANYHOW_EXECUTABLE_PATH
        );

        if (!StringUtils.isBlank(anyhowExecutablePath)) {
            try {
                return ExecutableUtil.getConfigurations(environment, credentials);
            } catch (Exception e) {
                String errorMessage = String.format("Could not retrieve Guacamole Configurations from Executable: %s", anyhowExecutablePath);
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        }

        return null;
    }

}
