package dev.samloh.guacamole_extensions.anyhow;

import dev.samloh.guacamole_extensions.anyhow.model.guacamole.AnyhowGuacamoleAuthenticatedUser;
import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowConfiguration;
import dev.samloh.guacamole_extensions.anyhow.util.ParserUtil;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.Credentials;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnyhowAuthenticationProviderTest {
    private static final Logger logger = LoggerFactory.getLogger(AnyhowAuthenticationProviderTest.class);

    @Test
    void testAnyhowAuthenticationProvider() throws GuacamoleException {
        AnyhowAuthenticationProvider anyhowAuthenticationProvider = new AnyhowAuthenticationProvider() {
            @Override
            public AnyhowConfiguration loadConfiguration(Credentials credentials, Environment environment) throws GuacamoleException {
                try {
                    InputStream jsonStream = getClass().getClassLoader().getResourceAsStream("test.json");
                    return ParserUtil.mapAnyhowConfiguration(jsonStream, "json");
                } catch (Exception e) {
                    throw new GuacamoleException(e);
                }

            }
        };

        AnyhowGuacamoleAuthenticatedUser authenticatedUser = new AnyhowGuacamoleAuthenticatedUser();
        authenticatedUser.setIdentifier("test01");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        Credentials credentials = new Credentials("test01", "test02", request);

        authenticatedUser.setCredentials(credentials);
        authenticatedUser.setAuthenticationProvider(anyhowAuthenticationProvider);

        anyhowAuthenticationProvider.getUserContext(authenticatedUser);

    }
}
