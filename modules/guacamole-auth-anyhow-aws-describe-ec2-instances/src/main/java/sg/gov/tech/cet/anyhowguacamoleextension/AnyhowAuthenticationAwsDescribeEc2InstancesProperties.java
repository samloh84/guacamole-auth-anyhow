package sg.gov.tech.cet.anyhowguacamoleextension;

import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

public abstract class AnyhowAuthenticationAwsDescribeEc2InstancesProperties extends AnyhowAuthenticationProperties {
    public static final StringGuacamoleProperty ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_PROFILE =
            new StringGuacamoleProperty() {
                @Override
                public String getName() {
                    return "anyhow-aws-describe-ec2-instances-profile";
                }
            };

    public static final BooleanGuacamoleProperty ANYHOW_AWS_DESCRIBE_EC2_INSTANCES = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-aws-describe-ec2-instances";
        }
    };


    public static final BooleanGuacamoleProperty ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_BY_INSTANCE_VPC_ID = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-aws-describe-ec2-instances-id";
        }
    };

    public static final StringGuacamoleProperty ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_VPC_ID = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-aws-describe-ec2-instances-id";
        }
    };
    public static final StringGuacamoleProperty ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_SUBNET_ID = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-aws-describe-ec2-instances-id";
        }
    };
    public static final StringGuacamoleProperty ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_INSTANCE_GROUP_NAME = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-aws-describe-ec2-instances-name";
        }
    };
    public static final StringGuacamoleProperty ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_INSTANCE_GROUP_ID = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-aws-describe-ec2-instances-id";
        }
    };


    public static final BooleanGuacamoleProperty ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_ALWAYS_AUTHENTICATE = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "anyhow-aws-describe-ec2-instances-authenticate";
        }
    };


}
