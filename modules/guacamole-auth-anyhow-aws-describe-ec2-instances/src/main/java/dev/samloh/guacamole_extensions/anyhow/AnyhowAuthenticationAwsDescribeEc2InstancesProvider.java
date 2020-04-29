package dev.samloh.guacamole_extensions.anyhow;

import dev.samloh.guacamole_extensions.anyhow.model.guacamole.AnyhowGuacamoleAuthenticatedUser;
import dev.samloh.guacamole_extensions.anyhow.model.guacamole.AnyhowGuacamoleUser;
import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowConfiguration;
import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowUser;
import dev.samloh.guacamole_extensions.anyhow.util.AwsDescribeEc2InstancesUtil;
import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyhowAuthenticationAwsDescribeEc2InstancesProvider extends AnyhowAuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(AnyhowAuthenticationAwsDescribeEc2InstancesProvider.class);

    public AnyhowAuthenticationAwsDescribeEc2InstancesProvider() throws GuacamoleException {
        super();
    }

    @Override
    public String getIdentifier() {
        return "anyhow-aws-describe-ec2-instances";
    }

    @Override
    public AuthenticatedUser authenticateUser(Credentials credentials) throws GuacamoleException {

        Boolean alwaysAuthenticate = environment.getProperty(
                AnyhowAuthenticationAwsDescribeEc2InstancesProperties.ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_ALWAYS_AUTHENTICATE, false
        );

        if (alwaysAuthenticate) {
            AnyhowGuacamoleAuthenticatedUser authenticatedUser = new AnyhowGuacamoleAuthenticatedUser();
            authenticatedUser.setIdentifier(credentials.getUsername());
            authenticatedUser.setAuthenticationProvider(this);
            authenticatedUser.setCredentials(credentials);
            return authenticatedUser;
        }

        return super.authenticateUser(credentials);
    }

    @Override
    public AnyhowConfiguration loadConfiguration(Credentials credentials, Environment environment) throws GuacamoleException {

        Boolean describeEc2Instances = environment.getProperty(
                AnyhowAuthenticationAwsDescribeEc2InstancesProperties.ANYHOW_AWS_DESCRIBE_EC2_INSTANCES
                , false);

        if (describeEc2Instances) {

            try {
                return AwsDescribeEc2InstancesUtil.getConfigurations(environment, credentials);
            } catch (Exception e) {
                String errorMessage = "Could not retrieve Guacamole Configurations from AWS Describe EC2 Instances";
                GuacamoleClientException exception = new GuacamoleClientException(errorMessage, e);
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        }

        return null;
    }

}
