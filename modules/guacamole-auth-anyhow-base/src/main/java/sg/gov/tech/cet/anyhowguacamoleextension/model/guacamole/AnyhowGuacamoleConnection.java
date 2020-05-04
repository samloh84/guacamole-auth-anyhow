package sg.gov.tech.cet.anyhowguacamoleextension.model.guacamole;

import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.AnyhowConnection;
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

import java.util.*;

public class AnyhowGuacamoleConnection extends AbstractConnection {
    private Map<String, String> attributes = new HashMap<>();

    public AnyhowGuacamoleConnection() {
        this.setParentIdentifier("ROOT");
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        this.setConfiguration(configuration);
    }

    public AnyhowGuacamoleConnection(AnyhowConnection connection) {
        this();
        this.setName(connection.getName());
        this.setIdentifier(connection.getIdentifier());
        this.setAttributes(new HashMap<>(connection.getAttributes()));
        this.getConfiguration().setParameters(new HashMap<>(connection.getParameters()));
        this.getConfiguration().setProtocol(connection.getProtocol());
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
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
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
