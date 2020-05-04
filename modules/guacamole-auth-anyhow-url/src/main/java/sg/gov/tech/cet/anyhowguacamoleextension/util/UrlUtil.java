package sg.gov.tech.cet.anyhowguacamoleextension.util;


import sg.gov.tech.cet.anyhowguacamoleextension.AnyhowAuthenticationUrlProperties;
import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.AnyhowConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UrlUtil {
    public static AnyhowConfiguration getConfigurations(Environment environment, Credentials credentials) throws GuacamoleException, URISyntaxException, IOException {

        String username = credentials.getUsername();
        String remoteAddress = credentials.getRemoteAddress();
        String remoteHostname = credentials.getRemoteHostname();

        String anyhowUrl = environment.getProperty(
                AnyhowAuthenticationUrlProperties.ANYHOW_URL
        );
        String anyhowUrlFormat = StringUtils.defaultIfBlank(
                environment.getProperty(
                        AnyhowAuthenticationUrlProperties.ANYHOW_URL_FORMAT
                ), AnyhowAuthenticationUrlProperties.DEFAULT_ANYHOW_URL_FORMAT);
        String anyhowUrlUserAgent = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationUrlProperties.ANYHOW_URL_USER_AGENT
        ), AnyhowAuthenticationUrlProperties.DEFAULT_ANYHOW_URL_USER_AGENT);

        String anyhowUrlMethod = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationUrlProperties.ANYHOW_URL_METHOD
        ), AnyhowAuthenticationUrlProperties.DEFAULT_ANYHOW_URL_METHOD);
        String anyhowUrlUsernameParameter = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationUrlProperties.ANYHOW_URL_USERNAME_PARAMETER
        ), AnyhowAuthenticationUrlProperties.DEFAULT_ANYHOW_URL_USERNAME_PARAMETER);
        String anyhowUrlRemoteAddressParameter = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationUrlProperties.ANYHOW_URL_REMOTE_ADDRESS_PARAMETER
        ), AnyhowAuthenticationUrlProperties.DEFAULT_ANYHOW_URL_REMOTE_ADDRESS_PARAMETER);
        String anyhowUrlRemoteHostnameParameter = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationUrlProperties.ANYHOW_URL_REMOTE_HOSTNAME_PARAMETER
        ), AnyhowAuthenticationUrlProperties.DEFAULT_ANYHOW_URL_REMOTE_HOSTNAME_PARAMETER);
        String anyhowUrlApiKeyParameter = StringUtils.defaultIfBlank(environment.getProperty(
                AnyhowAuthenticationUrlProperties.ANYHOW_URL_API_KEY_PARAMETER
        ), AnyhowAuthenticationUrlProperties.DEFAULT_ANYHOW_URL_API_KEY_PARAMETER);
        String anyhowUrlApiKey = environment.getProperty(
                AnyhowAuthenticationUrlProperties.ANYHOW_URL_API_KEY
        );


        URIBuilder uriBuilder = new URIBuilder(anyhowUrl);
        HttpUriRequest request;

        if (StringUtils.lowerCase(anyhowUrlMethod).equals("get")) {


            if (!StringUtils.lowerCase(anyhowUrlUsernameParameter).equals("null")) {
                uriBuilder.addParameter(anyhowUrlUsernameParameter, username);
            }

            if (!StringUtils.lowerCase(anyhowUrlRemoteAddressParameter).equals("null")) {
                uriBuilder.addParameter(anyhowUrlRemoteAddressParameter, remoteAddress);
            }

            if (!StringUtils.lowerCase(anyhowUrlRemoteHostnameParameter).equals("null")) {
                uriBuilder.addParameter(anyhowUrlRemoteHostnameParameter, remoteHostname);
            }

            if (!StringUtils.isEmpty(anyhowUrlApiKey) && !StringUtils.lowerCase(anyhowUrlApiKeyParameter).equals("null")) {
                uriBuilder.addParameter(anyhowUrlApiKeyParameter, anyhowUrlApiKey);
            }

            URI requestUri = uriBuilder.build();

            request = new HttpGet(requestUri);

        } else if (StringUtils.lowerCase(anyhowUrlMethod).equals("post")) {

            List<NameValuePair> urlParameters = new ArrayList<>();

            if (!StringUtils.lowerCase(anyhowUrlUsernameParameter).equals("null")) {
                urlParameters.add(new BasicNameValuePair(anyhowUrlUsernameParameter, username));
            }

            if (!StringUtils.lowerCase(anyhowUrlRemoteAddressParameter).equals("null")) {
                urlParameters.add(new BasicNameValuePair(anyhowUrlRemoteAddressParameter, remoteAddress));
            }

            if (!StringUtils.lowerCase(anyhowUrlRemoteHostnameParameter).equals("null")) {
                urlParameters.add(new BasicNameValuePair(anyhowUrlRemoteHostnameParameter, remoteHostname));
            }

            if (!StringUtils.isEmpty(anyhowUrlApiKey) && !StringUtils.lowerCase(anyhowUrlApiKeyParameter).equals("null")) {
                urlParameters.add(new BasicNameValuePair(anyhowUrlApiKeyParameter, anyhowUrlApiKey));
            }

            HttpPost postRequest = new HttpPost(anyhowUrl);
            if (!urlParameters.isEmpty()) {
                postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            }

            request = postRequest;
        } else {
            throw new Error(String.format("Invalid URL method \"%s\"", anyhowUrlMethod));
        }

        request.addHeader(HttpHeaders.USER_AGENT, anyhowUrlUserAgent);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                return ParserUtil.mapAnyhowConfiguration(entity.getContent(), anyhowUrlFormat);
            }

        }

        return null;

    }
}