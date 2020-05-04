package sg.gov.tech.cet.anyhowguacamoleextension.util;

import sg.gov.tech.cet.anyhowguacamoleextension.AnyhowAuthenticationProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

import java.util.Collection;

public class OverrideUtil {

    private OverrideUtil() {
    }

    public static void applyOverrides(Environment environment, Collection<Connection> connections) throws GuacamoleException {


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


        connections.forEach((connection) -> {
            GuacamoleConfiguration guacamoleConfiguration = connection.getConfiguration();
            if (guacamoleConfiguration.getProtocol().equals("ssh") && StringUtils.isNotBlank(overrideTypescriptPath)) {
                guacamoleConfiguration.setParameter("typescript-path", overrideTypescriptPath);

                if (overrideCreateTypescriptPath != null) {
                    guacamoleConfiguration.setParameter("create-typescript-path", String.valueOf(overrideCreateRecordingPath));
                }

                if (StringUtils.isNotBlank(overrideTypescriptName)) {
                    guacamoleConfiguration.setParameter("typescript-name", overrideTypescriptName);
                }
            }

            if ((guacamoleConfiguration.getProtocol().equals("rdp") || guacamoleConfiguration.getProtocol().equals("vnc")) && StringUtils.isNotBlank(overrideRecordingPath)) {
                guacamoleConfiguration.setParameter("recording-path", overrideRecordingPath);

                if (overrideCreateRecordingPath != null) {
                    guacamoleConfiguration.setParameter("create-recording-path", String.valueOf(overrideCreateRecordingPath));
                }

                if (StringUtils.isNotBlank(overrideRecordingName)) {
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

                if (StringUtils.isNotBlank(overrideSftpRootDirectory)) {
                    guacamoleConfiguration.setParameter("sftp-root-directory", overrideSftpRootDirectory);
                }
            }

        });

    }
}
