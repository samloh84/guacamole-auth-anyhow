package dev.samloh.guacamole_extensions.anyhow.util;


import dev.samloh.guacamole_extensions.anyhow.AnyhowAuthenticationExecutableProperties;
import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.Credentials;

import java.io.IOException;

public class ExecutableUtil {
    public static AnyhowConfiguration getConfigurations(Environment environment, Credentials credentials) throws GuacamoleException, IOException {
        String username = credentials.getUsername();
        String remoteAddress = credentials.getRemoteAddress();
        String remoteHostname = credentials.getRemoteHostname();

        String anyhowExecutablePath = environment.getProperty(
                AnyhowAuthenticationExecutableProperties.ANYHOW_EXECUTABLE_PATH
        );
        String anyhowExecutableFormat = StringUtils.defaultIfBlank(
                environment.getProperty(
                        AnyhowAuthenticationExecutableProperties.ANYHOW_EXECUTABLE_FORMAT
                ), AnyhowAuthenticationExecutableProperties.DEFAULT_ANYHOW_EXECUTABLE_FORMAT);
        String anyhowExecutableUsernameEnv = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationExecutableProperties.ANYHOW_EXECUTABLE_USERNAME_ENV
        ), AnyhowAuthenticationExecutableProperties.DEFAULT_ANYHOW_EXECUTABLE_USERNAME_ENV);
        String anyhowExecutableRemoteAddressEnv = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationExecutableProperties.ANYHOW_EXECUTABLE_REMOTE_ADDRESS_ENV
        ), AnyhowAuthenticationExecutableProperties.DEFAULT_ANYHOW_EXECUTABLE_REMOTE_ADDRESS_ENV);
        String anyhowExecutableRemoteHostnameEnv = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationExecutableProperties.ANYHOW_EXECUTABLE_REMOTE_HOSTNAME_ENV
        ), AnyhowAuthenticationExecutableProperties.DEFAULT_ANYHOW_EXECUTABLE_REMOTE_HOSTNAME_ENV);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(anyhowExecutablePath);

        if (!StringUtils.lowerCase(anyhowExecutableUsernameEnv).equals("null")) {
            processBuilder.environment().put(anyhowExecutableUsernameEnv, username);
        }

        if (!StringUtils.lowerCase(anyhowExecutableRemoteAddressEnv).equals("null")) {
            processBuilder.environment().put(anyhowExecutableRemoteAddressEnv, remoteAddress);
        }

        if (!StringUtils.lowerCase(anyhowExecutableRemoteHostnameEnv).equals("null")) {
            processBuilder.environment().put(anyhowExecutableRemoteHostnameEnv, remoteHostname);
        }

        Process process = processBuilder.start();

        return ParserUtil.mapAnyhowConfiguration(process.getInputStream(), anyhowExecutableFormat);
    }
}