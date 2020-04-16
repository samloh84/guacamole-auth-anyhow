package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowUser;
import org.apache.guacamole.net.auth.AbstractUser;

import java.util.Map;

public class AnyhowGuacamoleUser extends AbstractUser {

    private AnyhowUser user;


    public AnyhowGuacamoleUser(AnyhowUser user) {
        this.user = user;
    }

    public AnyhowUser getUser() {
        return user;
    }

    public void setUser(AnyhowUser user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public void setPassword(String password) {
        user.setPassword(password);
    }

    @Override
    public Map<String, String> getAttributes() {
        return user.getAttributes();
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        user.setAttributes(attributes);
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
