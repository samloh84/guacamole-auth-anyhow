package sg.gov.tech.cet.anyhowguacamoleextension.util;


import sg.gov.tech.cet.anyhowguacamoleextension.AnyhowAuthenticationFileProperties;
import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.AnyhowConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static long lastModified = Long.MIN_VALUE;
    private static AnyhowConfiguration cachedConfigurations = null;

    public static AnyhowConfiguration getConfigurations(Environment environment) throws GuacamoleException, IOException {
        String anyhowFilePath = environment.getProperty(
                AnyhowAuthenticationFileProperties.ANYHOW_FILE_PATH
        );
        String anyhowFileFormat = StringUtils.defaultIfBlank(
                environment.getProperty(
                        AnyhowAuthenticationFileProperties.ANYHOW_FILE_FORMAT
                ), AnyhowAuthenticationFileProperties.DEFAULT_ANYHOW_FILE_FORMAT);

        File anyhowFile = new File(environment.getGuacamoleHome(), anyhowFilePath);
        if (!anyhowFile.exists()) {
            throw new Error(String.format("Path \"%s\" does not exist", anyhowFile.getAbsolutePath()));
        }

        if (lastModified < anyhowFile.lastModified()) {
            try (FileInputStream inputStream = new FileInputStream(anyhowFile)) {
                cachedConfigurations = ParserUtil.mapAnyhowConfiguration(inputStream, anyhowFileFormat);
                lastModified = anyhowFile.lastModified();
                logger.info("Loaded file configuration: {}", cachedConfigurations);
            }
        } else {
            logger.info("Using cached file configuration: {}", cachedConfigurations);
        }

        return cachedConfigurations;
    }
}