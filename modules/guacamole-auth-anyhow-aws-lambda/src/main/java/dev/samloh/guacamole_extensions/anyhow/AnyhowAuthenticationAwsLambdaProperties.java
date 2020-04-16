package dev.samloh.guacamole_extensions.anyhow;

import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

public abstract class AnyhowAuthenticationAwsLambdaProperties extends AnyhowAuthenticationProperties {
    public static final StringGuacamoleProperty ANYHOW_AWS_LAMBDA_PROFILE =
            new StringGuacamoleProperty() {
                @Override
                public String getName() {
                    return "anyhow-aws-lambda-profile";
                }
            };

    public static final StringGuacamoleProperty ANYHOW_AWS_LAMBDA_FUNCTION =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-aws-lambda-function";
                }
            };
    public static final StringGuacamoleProperty ANYHOW_AWS_LAMBDA_USERNAME_KEY =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-aws-lambda-key";
                }
            };
    public static final String DEFAULT_ANYHOW_AWS_LAMBDA_USERNAME_KEY = "GUACAMOLE_USERNAME";
    public static final StringGuacamoleProperty ANYHOW_AWS_LAMBDA_REMOTE_ADDRESS_KEY =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-aws-lambda-key";
                }
            };
    public static final String DEFAULT_ANYHOW_AWS_LAMBDA_REMOTE_ADDRESS_KEY = "GUACAMOLE_REMOTE_ADDRESS";
    public static final StringGuacamoleProperty ANYHOW_AWS_LAMBDA_REMOTE_HOSTNAME_KEY =
            new StringGuacamoleProperty() {

                @Override
                public String getName() {
                    return "anyhow-aws-lambda-key";
                }
            };
    public static final String DEFAULT_ANYHOW_AWS_LAMBDA_REMOTE_HOSTNAME_KEY = "GUACAMOLE_REMOTE_HOSTNAME";


    public static final BooleanGuacamoleProperty ANYHOW_AWS_LAMBDA_ALWAYS_AUTHENTICATE = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-aws-lambda-authenticate";
        }
    };


}
