package dev.samloh.guacamole_extensions.anyhow;

import org.apache.guacamole.properties.StringGuacamoleProperty;

public abstract class AnyhowAuthenticationUrlProperties extends AnyhowAuthenticationProperties {

    public static final StringGuacamoleProperty ANYHOW_URL =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-url";
                }
            };
    public static final StringGuacamoleProperty ANYHOW_URL_METHOD =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-url-method";
                }
            };
    public static final String DEFAULT_ANYHOW_URL_METHOD = "get";
    public static final StringGuacamoleProperty ANYHOW_URL_FORMAT =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-url-format";
                }
            };
    public static final String DEFAULT_ANYHOW_URL_FORMAT = "json";
    public static final StringGuacamoleProperty ANYHOW_URL_USERNAME_PARAMETER =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-url-username-parameter";
                }
            };
    public static final String DEFAULT_ANYHOW_URL_USERNAME_PARAMETER = "username";
    public static final StringGuacamoleProperty ANYHOW_URL_REMOTE_ADDRESS_PARAMETER =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-url-remote-address-parameter";
                }
            };
    public static final String DEFAULT_ANYHOW_URL_REMOTE_ADDRESS_PARAMETER = "remote_address";
    public static final StringGuacamoleProperty ANYHOW_URL_REMOTE_HOSTNAME_PARAMETER =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-url-remote-hostname-parameter";
                }
            };
    public static final String DEFAULT_ANYHOW_URL_REMOTE_HOSTNAME_PARAMETER = "remote_hostname";
    public static final StringGuacamoleProperty ANYHOW_URL_USER_AGENT = new StringGuacamoleProperty() {

        @Override
        public String getName() {
            return "anyhow-url-user-agent";
        }
    };
    public static final String DEFAULT_ANYHOW_URL_USER_AGENT = "Apache Guacamole Anyhow Extension";
    public static final StringGuacamoleProperty ANYHOW_URL_API_KEY =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-url-api-key";
                }
            };
    public static final StringGuacamoleProperty ANYHOW_URL_API_KEY_PARAMETER =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-url-api-key-parameter";
                }
            };
    public static final String DEFAULT_ANYHOW_URL_API_KEY_PARAMETER = "key";


}
