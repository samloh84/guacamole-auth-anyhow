package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import dev.samloh.guacamole_extensions.anyhow.AnyhowAuthenticationProvider;
import org.apache.guacamole.net.auth.AbstractAuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;

import java.util.Collections;
import java.util.Set;

public class AnyhowGuacamoleAuthenticatedUser extends AbstractAuthenticatedUser {
    AnyhowAuthenticationProvider authenticationProvider;
    Credentials credentials;
    AnyhowGuacamoleUser user;

    public AnyhowGuacamoleAuthenticatedUser(AnyhowAuthenticationProvider authenticationProvider, Credentials credentials, AnyhowGuacamoleUser user) {
        this.authenticationProvider = authenticationProvider;
        this.credentials = credentials;
        this.user = user;
    }

    @Override
    public AnyhowAuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(AnyhowAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public AnyhowGuacamoleUser getUser() {
        return user;
    }

    public void setUser(AnyhowGuacamoleUser user) {
        this.user = user;
    }


    @Override
    public Set<String> getEffectiveUserGroups() {
        return Collections.emptySet();
    }

    @Override
    public String getIdentifier() {
        return user.getIdentifier();
    }

    @Override
    public void setIdentifier(String identifier) {
        user.setIdentifier(identifier);
    }


}
