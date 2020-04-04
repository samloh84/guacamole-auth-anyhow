package dev.samloh.guacamole_extensions.anyhow.util;


import dev.samloh.guacamole_extensions.anyhow.AnyhowAuthenticationAwsLambdaProperties;
import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.Credentials;
import org.codehaus.jackson.map.ObjectMapper;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.LambdaClientBuilder;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AwsLambdaUtil {
    public static AnyhowConfiguration getConfigurations(Environment environment, Credentials credentials) throws GuacamoleException, IOException {
        String username = credentials.getUsername();
        String remoteAddress = credentials.getRemoteAddress();
        String remoteHostname = credentials.getRemoteHostname();

        String awsProfile = environment.getProperty(
                AnyhowAuthenticationAwsLambdaProperties.AWS_LAMBDA_PROFILE
        );

        String awsLambdaFunction = environment.getProperty(
                AnyhowAuthenticationAwsLambdaProperties.AWS_LAMBDA_FUNCTION
        );

        String awsLambdaUsernameKey = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationAwsLambdaProperties.AWS_LAMBDA_USERNAME_KEY
        ), AnyhowAuthenticationAwsLambdaProperties.DEFAULT_AWS_LAMBDA_USERNAME_KEY);
        String awsLambdaRemoteAddressKey = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationAwsLambdaProperties.AWS_LAMBDA_REMOTE_ADDRESS_KEY
        ), AnyhowAuthenticationAwsLambdaProperties.DEFAULT_AWS_LAMBDA_REMOTE_ADDRESS_KEY);
        String awsLambdaRemoteHostnameKey = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationAwsLambdaProperties.AWS_LAMBDA_REMOTE_HOSTNAME_KEY
        ), AnyhowAuthenticationAwsLambdaProperties.DEFAULT_AWS_LAMBDA_REMOTE_HOSTNAME_KEY);

        Map<String, String> lambdaInput = new HashMap<>();

        if (!StringUtils.lowerCase(awsLambdaUsernameKey).equals("null")) {
            lambdaInput.put(awsLambdaUsernameKey, username);
        }

        if (!StringUtils.lowerCase(awsLambdaRemoteHostnameKey).equals("null")) {
            lambdaInput.put(awsLambdaRemoteHostnameKey, remoteHostname);
        }

        if (!StringUtils.lowerCase(awsLambdaRemoteAddressKey).equals("null")) {
            lambdaInput.put(awsLambdaRemoteAddressKey, remoteAddress);
        }

        ObjectMapper mapper = new ObjectMapper();


        LambdaClientBuilder lambdaClientBuilder = LambdaClient.builder();

        if (StringUtils.isNotBlank(awsProfile)) {
            lambdaClientBuilder.credentialsProvider(ProfileCredentialsProvider.builder().profileName(awsProfile).build());
        }

        LambdaClient lambdaClient = lambdaClientBuilder.build();
        InvokeRequest.Builder requestBuilder = InvokeRequest.builder()
                .functionName(awsLambdaFunction)
                .invocationType(InvocationType.REQUEST_RESPONSE);
        if (!lambdaInput.isEmpty()) {
            requestBuilder.payload(SdkBytes.fromByteArray(mapper.writeValueAsBytes(lambdaInput)));
        }

        InvokeResponse response = lambdaClient.invoke(requestBuilder.build());

        return ParserUtil.mapAnyhowConfiguration(response.payload().asInputStream(), "json");
    }
}