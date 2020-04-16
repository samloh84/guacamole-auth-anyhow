package dev.samloh.guacamole_extensions.anyhow;


import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

public abstract class AnyhowAuthenticationExecutableProperties extends AnyhowAuthenticationProperties {

    public static final StringGuacamoleProperty ANYHOW_EXECUTABLE_PATH =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-executable-path";
                }
            };
    public static final StringGuacamoleProperty ANYHOW_EXECUTABLE_FORMAT =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-executable-format";
                }

            };
    public static final String DEFAULT_ANYHOW_EXECUTABLE_FORMAT = "json";
    public static final StringGuacamoleProperty ANYHOW_EXECUTABLE_USERNAME_ENV =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-executable-username-env";
                }
            };
    public static final String DEFAULT_ANYHOW_EXECUTABLE_USERNAME_ENV = "GUACAMOLE_USERNAME";
    public static final StringGuacamoleProperty ANYHOW_EXECUTABLE_REMOTE_ADDRESS_ENV =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-executable-remote-address-env";
                }
            };
    public static final String DEFAULT_ANYHOW_EXECUTABLE_REMOTE_ADDRESS_ENV = "GUACAMOLE_REMOTE_ADDRESS";
    public static final StringGuacamoleProperty ANYHOW_EXECUTABLE_REMOTE_HOSTNAME_ENV =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-executable-remote-hostname-env";
                }
            };
    public static final String DEFAULT_ANYHOW_EXECUTABLE_REMOTE_HOSTNAME_ENV = "GUACAMOLE_REMOTE_HOSTNAME";


    public static final BooleanGuacamoleProperty ANYHOW_EXECUTABLE_ALWAYS_AUTHENTICATE = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-executable-always-authenticate";
        }
    };
}
