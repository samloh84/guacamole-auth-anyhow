package dev.samloh.guacamole_extensions.anyhow.util;


import dev.samloh.guacamole_extensions.anyhow.AnyhowAuthenticationProperties;
import dev.samloh.guacamole_extensions.anyhow.model.AnyhowGuacamoleConfigurations;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class AnyhowGuacamoleFileConfiguration {

    private static long lastModified = Long.MIN_VALUE;
    private static AnyhowGuacamoleConfigurations cachedConfigurations = null;

    public static AnyhowGuacamoleConfigurations getConfigurations(Environment environment) throws GuacamoleException, URISyntaxException, IOException {
        String anyhowFilePath = environment.getProperty(
                AnyhowAuthenticationProperties.ANYHOW_FILE_PATH
        );
        String anyhowFileFormat = StringUtils.defaultIfBlank(
                environment.getProperty(
                        AnyhowAuthenticationProperties.ANYHOW_FILE_FORMAT
                ), AnyhowAuthenticationProperties.DEFAULT_ANYHOW_FILE_FORMAT);

        File anyhowFile = new File(environment.getGuacamoleHome(), anyhowFilePath);
        if (!anyhowFile.exists()) {
            throw new Error(String.format("Path \"%s\" does not exist", anyhowFilePath));
        }

        if (lastModified < anyhowFile.lastModified()) {
            try (FileInputStream inputStream = new FileInputStream(anyhowFile)) {
                cachedConfigurations = AnyhowGuacamoleConfigurationsMapper.mapAnyhowGuacamoleConfigurations(inputStream, anyhowFileFormat);
                lastModified = anyhowFile.lastModified();
            }
        }
        return cachedConfigurations;


    }
}