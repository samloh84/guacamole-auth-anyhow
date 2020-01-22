package dev.samloh.guacamole_extensions.anyhow;

import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

public class AnyhowAuthenticationProperties {

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
    public static final StringGuacamoleProperty ANYHOW_OVERRIDE_TYPESCRIPT_PATH = new StringGuacamoleProperty() {

        @Override
        public String getName() {
            return "anyhow-override-typescript-path";
        }
    };
    public static final BooleanGuacamoleProperty ANYHOW_OVERRIDE_CREATE_TYPESCRIPT_PATH = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-override-create-typescript-path";
        }
    };
    public static final StringGuacamoleProperty ANYHOW_OVERRIDE_TYPESCRIPT_NAME = new StringGuacamoleProperty() {

        @Override
        public String getName() {
            return "anyhow-override-typescript-name";
        }
    };
    public static final StringGuacamoleProperty ANYHOW_OVERRIDE_RECORDING_PATH = new StringGuacamoleProperty() {

        @Override
        public String getName() {
            return "anyhow-override-recording-path";
        }
    };
    public static final BooleanGuacamoleProperty ANYHOW_OVERRIDE_CREATE_RECORDING_PATH = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-override-create-recording-path";
        }
    };
    public static final StringGuacamoleProperty ANYHOW_OVERRIDE_RECORDING_NAME = new StringGuacamoleProperty() {

        @Override
        public String getName() {
            return "anyhow-override-recording-name";
        }
    };
    public static final BooleanGuacamoleProperty ANYHOW_OVERRIDE_RECORDING_EXCLUDE_OUTPUT = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-override-recording-exclude-output";
        }
    };
    public static final BooleanGuacamoleProperty ANYHOW_OVERRIDE_RECORDING_EXCLUDE_MOUSE = new BooleanGuacamoleProperty() {

        @Override
        public String getName() {
            return "anyhow-override-recording-exclude-mouse";
        }
    };
    public static final BooleanGuacamoleProperty ANYHOW_OVERRIDE_RECORDING_INCLUDE_KEYS = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-override-recording-include-keys";
        }
    };
    public static final BooleanGuacamoleProperty ANYHOW_OVERRIDE_ENABLE_SFTP = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-override-enable-sftp";
        }
    };
    public static final StringGuacamoleProperty ANYHOW_OVERRIDE_SFTP_ROOT_DIRECTORY = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-override-sftp-root-directory";
        }
    };


    public static final BooleanGuacamoleProperty ANYHOW_ALWAYS_AUTHENTICATE = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-always-authenticate";
        }
    };




    private AnyhowAuthenticationProperties() {
    }


}
