package dev.samloh.guacamole_extensions.anyhow;

import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

public abstract class AnyhowAuthenticationFileProperties extends AnyhowAuthenticationProperties {

    public static final StringGuacamoleProperty ANYHOW_FILE_PATH =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-file-path";
                }

            };
    public static final StringGuacamoleProperty ANYHOW_FILE_FORMAT =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-file-format";
                }

            };
    public static final String DEFAULT_ANYHOW_FILE_FORMAT = "json";

    public static final BooleanGuacamoleProperty ANYHOW_FILE_ALWAYS_AUTHENTICATE = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-file-always-authenticate";
        }
    };

}
