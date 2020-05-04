package sg.gov.tech.cet.anyhowguacamoleextension.util;

import sg.gov.tech.cet.anyhowguacamoleextension.AnyhowAuthenticationAwsDescribeEc2InstancesProperties;
import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.AnyhowConfiguration;
import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.AnyhowConnection;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.Credentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.Ec2ClientBuilder;
import software.amazon.awssdk.services.ec2.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AwsDescribeEc2InstancesUtil {


    public static AnyhowConfiguration getConfigurations(Environment environment, Credentials credentials) throws GuacamoleException, IOException {
        String username = credentials.getUsername();
        String remoteAddress = credentials.getRemoteAddress();
        String remoteHostname = credentials.getRemoteHostname();


        String awsProfile = environment.getProperty(
                AnyhowAuthenticationAwsDescribeEc2InstancesProperties.ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_PROFILE);


        Boolean filterByInstanceVpcId = environment.getProperty(AnyhowAuthenticationAwsDescribeEc2InstancesProperties.ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_BY_INSTANCE_VPC_ID, false);
        String filterVpcId = environment.getProperty(AnyhowAuthenticationAwsDescribeEc2InstancesProperties.ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_VPC_ID);
        String filterSubnetId = environment.getProperty(AnyhowAuthenticationAwsDescribeEc2InstancesProperties.ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_SUBNET_ID);
        String filterInstanceGroupName = environment.getProperty(AnyhowAuthenticationAwsDescribeEc2InstancesProperties.ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_INSTANCE_GROUP_NAME);
        String filterInstanceGroupId = environment.getProperty(AnyhowAuthenticationAwsDescribeEc2InstancesProperties.ANYHOW_AWS_DESCRIBE_EC2_INSTANCES_FILTER_INSTANCE_GROUP_ID);

        Ec2ClientBuilder ec2ClientBuilder = Ec2Client.builder();

        if (StringUtils.isNotBlank(awsProfile)) {
            ec2ClientBuilder.credentialsProvider(ProfileCredentialsProvider.builder().profileName(awsProfile).build());
        }

        Ec2Client ec2 = ec2ClientBuilder.build();
        DescribeInstancesRequest.Builder describeInstancesRequestBuilder = DescribeInstancesRequest.builder();

        List<Filter> filters = new ArrayList<>();

        if (filterByInstanceVpcId) {
            String thisInstanceId = EC2MetadataUtils.getInstanceId();
            DescribeInstancesRequest describeThisInstanceRequest = DescribeInstancesRequest.builder().instanceIds(thisInstanceId).build();
            DescribeInstancesResponse describeThisInstanceResponse = ec2.describeInstances(describeThisInstanceRequest);
            String vpcId = describeThisInstanceResponse.reservations().get(0).instances().get(0).vpcId();
            filters.add(Filter.builder().name("vpc-id").values(vpcId).build());
        }


        if (!StringUtils.isBlank(filterInstanceGroupId)) {
            String[] instanceGroupIds = StringUtils.split(filterInstanceGroupId, ", ");
            filters.add(Filter.builder()
                    .name("instance.group-id")
                    .values(instanceGroupIds)
                    .build());
        }
        if (!StringUtils.isBlank(filterInstanceGroupName)) {
            String[] instanceGroupNames = StringUtils.split(filterInstanceGroupName, ", ");
            filters.add(Filter.builder()
                    .name("instance.group-name")
                    .values(instanceGroupNames)
                    .build());
        }

        if (!StringUtils.isBlank(filterVpcId)) {
            String[] vpcIds = StringUtils.split(filterVpcId, ", ");
            filters.add(Filter.builder()
                    .name("vpc-id")
                    .values(vpcIds)
                    .build());
        }

        if (!StringUtils.isBlank(filterSubnetId)) {
            String[] subnetIds = StringUtils.split(filterSubnetId, ", ");
            filters.add(Filter.builder()
                    .name("subnet-id")
                    .values(subnetIds)
                    .build());
        }

        if (!filters.isEmpty()) {
            describeInstancesRequestBuilder.filters(filters);
        }

        DescribeInstancesRequest describeInstancesRequest = describeInstancesRequestBuilder.build();
        List<Instance> instances = new ArrayList<>();
        DescribeInstancesResponse response = ec2.describeInstances(describeInstancesRequest);


        AnyhowConfiguration configuration = new AnyhowConfiguration();
        configuration.setConnections(new ArrayList<>());
        for (Reservation reservation : response.reservations()) {
            for (Instance instance : reservation.instances()) {
                AnyhowConnection connection = new AnyhowConnection();

                String name = null;
                for (Tag tag : instance.tags()) {
                    if (tag.key().equals("Name") || tag.key().equals("name")) {
                        name = tag.value();
                    }
                }

                String instanceId = instance.instanceId();
                String privateIpAddress = instance.privateIpAddress();
                String vpcId = instance.vpcId();

                String identifier;
                if (name != null) {
                    identifier = String.format("name=%s, privateIpAddress=%s, instanceId=%s, vpcId=%s", name, privateIpAddress, instanceId, vpcId);
                } else {
                    identifier = String.format("privateIpAddress=%s, instanceId=%s, vpcId=%s", privateIpAddress, instanceId, vpcId);
                }

                connection.setIdentifier(identifier);

                HashMap<String, String> parameters = new HashMap<>();

                if (instance.platform().equals(PlatformValues.WINDOWS)) {
                    connection.setProtocol("rdp");
                    parameters.put("hostname", instance.privateIpAddress());
                    parameters.put("port", "3306");
                } else {
                    connection.setProtocol("ssh");
                    parameters.put("hostname", instance.privateIpAddress());
                    parameters.put("port", "22");
                }

                connection.setParameters(parameters);
                configuration.getConnections().add(connection);


            }
        }


        return configuration;
    }


}
