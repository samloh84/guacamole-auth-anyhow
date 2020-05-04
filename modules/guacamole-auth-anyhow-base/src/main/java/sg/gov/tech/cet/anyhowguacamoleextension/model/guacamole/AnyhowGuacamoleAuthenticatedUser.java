package sg.gov.tech.cet.anyhowguacamoleextension.model.guacamole;

import org.apache.guacamole.net.auth.AbstractAuthenticatedUser;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;

public class AnyhowGuacamoleAuthenticatedUser extends AbstractAuthenticatedUser {

    private AuthenticationProvider authenticationProvider;
    private Credentials credentials;

    public AnyhowGuacamoleAuthenticatedUser() {
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return this.authenticationProvider;
    }

    @Override
    public Credentials getCredentials() {
        return this.credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
