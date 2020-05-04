package sg.gov.tech.cet.anyhowguacamoleextension.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.AnyhowConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ParserUtil {

    private ParserUtil() {
    }

    private static ObjectMapper createMapper(String format) {
        ObjectMapper mapper;

        switch (StringUtils.lowerCase(format)) {
            case "yaml":
            case "yml":
                mapper = new ObjectMapper(new YAMLFactory());
                break;
            case "xml":
                mapper = new ObjectMapper(new XmlFactory());
                break;
            case "json":
                mapper = new ObjectMapper(new JsonFactory());
                break;
            default:
                throw new Error(String.format("Invalid format: %s", format));
        }
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    public static AnyhowConfiguration mapAnyhowConfiguration(InputStream input, String format) throws IOException {
        return createMapper(format).readValue(input, AnyhowConfiguration.class);
    }

    public static AnyhowConfiguration mapAnyhowConfiguration(String input, String format) throws IOException {
        return createMapper(format).readValue(input, AnyhowConfiguration.class);
    }

    public static AnyhowConfiguration mapAnyhowConfiguration(File input, String format) throws IOException {
        return createMapper(format).readValue(input, AnyhowConfiguration.class);
    }

}
