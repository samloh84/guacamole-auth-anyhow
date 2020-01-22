package dev.samloh.guacamole_extensions.anyhow.util;


import dev.samloh.guacamole_extensions.anyhow.AnyhowAuthenticationProperties;
import dev.samloh.guacamole_extensions.anyhow.model.Configurations;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {

    private static long lastModified = Long.MIN_VALUE;
    private static Configurations cachedConfigurations = null;

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);


    public static Configurations getConfigurations(Environment environment) throws GuacamoleException, IOException {
        String anyhowFilePath = environment.getProperty(
                AnyhowAuthenticationProperties.ANYHOW_FILE_PATH
        );
        String anyhowFileFormat = StringUtils.defaultIfBlank(
                environment.getProperty(
                        AnyhowAuthenticationProperties.ANYHOW_FILE_FORMAT
                ), AnyhowAuthenticationProperties.DEFAULT_ANYHOW_FILE_FORMAT);

        File anyhowFile = new File(environment.getGuacamoleHome(), anyhowFilePath);
        if (!anyhowFile.exists()) {
            throw new Error(String.format("Path \"%s\" does not exist", anyhowFile.getAbsolutePath()));
        }

        if (lastModified < anyhowFile.lastModified()) {
            try (FileInputStream inputStream = new FileInputStream(anyhowFile)) {
                cachedConfigurations = ParserUtil.mapAnyhowGuacamoleConfigurations(inputStream, anyhowFileFormat);
                lastModified = anyhowFile.lastModified();
            }
        }

        logger.info("Obtained file configuration: {}", cachedConfigurations);

        return cachedConfigurations;


    }
}