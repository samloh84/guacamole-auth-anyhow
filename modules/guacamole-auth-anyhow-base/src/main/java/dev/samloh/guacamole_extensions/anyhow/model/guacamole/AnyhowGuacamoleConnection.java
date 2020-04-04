package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowConnection;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.GuacamoleServerException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.net.*;
import org.apache.guacamole.net.auth.AbstractConnection;
import org.apache.guacamole.net.auth.ConnectionRecord;
import org.apache.guacamole.net.auth.GuacamoleProxyConfiguration;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AnyhowGuacamoleConnection extends AbstractConnection {

    private AnyhowConnection connection;

    public AnyhowGuacamoleConnection(AnyhowConnection connection) {
        this.connection = connection;
    }


    public AnyhowConnection getConnection() {
        return connection;
    }

    public void setConnection(AnyhowConnection connection) {
        this.connection = connection;
    }


    @Override
    public String getName() {
        return connection.getName();
    }

    @Override
    public void setName(String name) {
        connection.setName(name);
    }

    @Override
    public GuacamoleConfiguration getConfiguration() {
        return connection.toGuacamoleConfiguration();
    }

    @Override
    public void setConfiguration(GuacamoleConfiguration configuration) {
        connection.setProtocol(configuration.getProtocol());
        connection.setParameters(configuration.getParameters());
    }

    @Override
    public String getIdentifier() {
        return connection.getIdentifier();
    }

    @Override
    public void setIdentifier(String identifier) {
        connection.setIdentifier(identifier);
    }

    @Override
    public Map<String, String> getAttributes() {
        return connection.getAttributes();
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        connection.setAttributes(attributes);
    }

    @Override
    public Date getLastActive() {
        return null;
    }

    @Override
    public List<? extends ConnectionRecord> getHistory() throws GuacamoleException {
        return Collections.emptyList();
    }

    @Override
    public GuacamoleTunnel connect(GuacamoleClientInformation info) throws GuacamoleException {

        // Retrieve proxy configuration from environment
        Environment environment = new LocalEnvironment();
        GuacamoleProxyConfiguration proxyConfig = environment.getDefaultGuacamoleProxyConfiguration();

        // Get guacd connection parameters
        String hostname = proxyConfig.getHostname();
        int port = proxyConfig.getPort();

        GuacamoleSocket socket;

        // Determine socket type based on required encryption method
        switch (proxyConfig.getEncryptionMethod()) {

            // If guacd requires SSL, use it
            case SSL:
                socket = new ConfiguredGuacamoleSocket(
                        new SSLGuacamoleSocket(hostname, port),
                        getConfiguration(), info
                );
                break;

            // Connect directly via TCP if encryption is not enabled
            case NONE:
                socket = new ConfiguredGuacamoleSocket(
                        new InetGuacamoleSocket(hostname, port),
                        getConfiguration(), info
                );
                break;

            // Abort if encryption method is unknown
            default:
                throw new GuacamoleServerException("Unimplemented encryption method.");

        }

        return new SimpleGuacamoleTunnel(socket);

    }


    @Override
    public int getActiveConnections() {
        return 0;
    }


}
