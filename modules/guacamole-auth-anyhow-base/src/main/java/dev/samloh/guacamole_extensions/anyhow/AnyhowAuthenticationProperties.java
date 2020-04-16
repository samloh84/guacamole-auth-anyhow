package dev.samloh.guacamole_extensions.anyhow;

import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

public abstract class AnyhowAuthenticationProperties {

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


}
