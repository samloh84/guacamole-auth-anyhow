package dev.samloh.guacamole_extensions.anyhow.util;

import dev.samloh.guacamole_extensions.anyhow.AnyhowAuthenticationProperties;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

import java.util.Map;

public class AnyhowGuacamoleConfigurationsApplyOverrides {
    public static void applyOverrides(Environment environment, Map<String, GuacamoleConfiguration> configurations) throws GuacamoleException {


        String overrideTypescriptName = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_TYPESCRIPT_NAME);
        String overrideTypescriptPath = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_TYPESCRIPT_PATH);
        Boolean overrideCreateTypescriptPath = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_CREATE_TYPESCRIPT_PATH);

        String overrideRecordingName = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_RECORDING_NAME);
        String overrideRecordingPath = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_RECORDING_PATH);
        Boolean overrideCreateRecordingPath = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_CREATE_RECORDING_PATH);
        Boolean overrideRecordingExcludeOutput = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_RECORDING_EXCLUDE_OUTPUT);
        Boolean overrideRecordingExcludeMouse = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_RECORDING_EXCLUDE_MOUSE);
        Boolean overrideRecordingIncludeKeys = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_RECORDING_INCLUDE_KEYS);

        Boolean overrideEnableSftp = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_ENABLE_SFTP);
        String overrideSftpRootDirectory = environment.getProperty(AnyhowAuthenticationProperties.ANYHOW_OVERRIDE_SFTP_ROOT_DIRECTORY);


        configurations.forEach((key, guacamoleConfiguration) -> {
            if (guacamoleConfiguration.getProtocol().equals("ssh") && overrideTypescriptPath != null) {
                guacamoleConfiguration.setParameter("typescript-path", overrideTypescriptPath);

                if (overrideCreateTypescriptPath != null) {
                    guacamoleConfiguration.setParameter("create-typescript-path", String.valueOf(overrideCreateTypescriptPath));
                }

                if (overrideTypescriptName != null) {
                    guacamoleConfiguration.setParameter("typescript-name", overrideTypescriptName);
                }
            }

            if (guacamoleConfiguration.getProtocol().equals("rdp") && overrideRecordingPath != null) {
                guacamoleConfiguration.setParameter("recording-path", overrideRecordingPath);

                if (overrideCreateRecordingPath != null) {
                    guacamoleConfiguration.setParameter("create-recording-path", String.valueOf(overrideCreateRecordingPath));
                }

                if (overrideRecordingName != null) {
                    guacamoleConfiguration.setParameter("recording-name", overrideRecordingName);
                }

                if (overrideRecordingExcludeMouse != null) {
                    guacamoleConfiguration.setParameter("recording-exclude-mouse", String.valueOf(overrideRecordingExcludeMouse));
                }

                if (overrideRecordingExcludeOutput != null) {
                    guacamoleConfiguration.setParameter("recording-exclude-output", String.valueOf(overrideRecordingExcludeOutput));
                }

                if (overrideRecordingIncludeKeys != null) {
                    guacamoleConfiguration.setParameter("recording-include-keys", String.valueOf(overrideRecordingIncludeKeys));
                }

            }

            if (overrideEnableSftp != null) {
                guacamoleConfiguration.setParameter("enable-sftp", String.valueOf(overrideEnableSftp));

                if (overrideSftpRootDirectory != null) {
                    guacamoleConfiguration.setParameter("sftp-root-directory", overrideSftpRootDirectory);
                }
            }

        });

    }
}
