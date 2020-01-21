package dev.samloh.guacamole_extensions.anyhow.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.samloh.guacamole_extensions.anyhow.model.AnyhowGuacamoleConfigurations;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

public class AnyhowGuacamoleConfigurationsMapper {

    public static AnyhowGuacamoleConfigurations mapAnyhowGuacamoleConfigurations(InputStream input, String format) throws IOException {
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

        return mapper.readValue(input, AnyhowGuacamoleConfigurations.class);
    }
}
