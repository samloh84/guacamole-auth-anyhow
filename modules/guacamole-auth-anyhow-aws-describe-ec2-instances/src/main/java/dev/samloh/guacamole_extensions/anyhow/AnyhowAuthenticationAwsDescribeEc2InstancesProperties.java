package dev.samloh.guacamole_extensions.anyhow;

import org.apache.guacamole.properties.BooleanGuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;

public abstract class AnyhowAuthenticationAwsDescribeEc2InstancesProperties extends AnyhowAuthenticationProperties {
    public static final StringGuacamoleProperty AWS_DESCRIBE_EC2_INSTANCES_PROFILE =
            new StringGuacamoleProperty() {
                @Override
                public String getName() {
                    return "aws-describe-ec2-instances-profile";
                }
            };

    public static final BooleanGuacamoleProperty AWS_DESCRIBE_EC2_INSTANCES = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "aws-describe-ec2-instances";
        }
    };


    public static final BooleanGuacamoleProperty AWS_DESCRIBE_EC2_INSTANCES_FILTER_BY_INSTANCE_VPC_ID = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "aws-describe-ec2-instances-filter-by-instance-vpc-id";
        }
    };

    public static final StringGuacamoleProperty AWS_DESCRIBE_EC2_INSTANCES_FILTER_VPC_ID = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "aws-describe-ec2-instances-filter-vpc-id";
        }
    };
    public static final StringGuacamoleProperty AWS_DESCRIBE_EC2_INSTANCES_FILTER_SUBNET_ID = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "aws-describe-ec2-instances-filter-subnet-id";
        }
    };
    public static final StringGuacamoleProperty AWS_DESCRIBE_EC2_INSTANCES_FILTER_INSTANCE_GROUP_NAME = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "aws-describe-ec2-instances-filter-instance-group-name";
        }
    };
    public static final StringGuacamoleProperty AWS_DESCRIBE_EC2_INSTANCES_FILTER_INSTANCE_GROUP_ID = new StringGuacamoleProperty() {
        @Override
        public String getName() {
            return "aws-describe-ec2-instances-filter-instance-group-id";
        }
    };


    public static final BooleanGuacamoleProperty AWS_DESCRIBE_EC2_INSTANCES_ALWAYS_AUTHENTICATE = new BooleanGuacamoleProperty() {
        @Override
        public String getName() {
            return "aws-describe-ec2-instances-always-authenticate";
        }
    };


}
