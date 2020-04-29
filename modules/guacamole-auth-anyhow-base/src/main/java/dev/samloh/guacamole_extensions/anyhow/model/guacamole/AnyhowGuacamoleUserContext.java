package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.net.auth.*;
import org.apache.guacamole.net.auth.simple.SimpleDirectory;

import java.util.ArrayList;
import java.util.Collection;

public class AnyhowGuacamoleUserContext extends AbstractUserContext {
    private User self;
    private AuthenticationProvider authenticationProvider;
    private Directory<User> userDirectory = new SimpleDirectory<>();
    private Directory<UserGroup> userGroupDirectory = new SimpleDirectory<>();
    private Directory<Connection> connectionDirectory = new SimpleDirectory<>();
    private Directory<ConnectionGroup> connectionGroupDirectory = new SimpleDirectory<>();
    private Collection<Form> userAttributes = new ArrayList<>();
    private Collection<Form> userGroupAttributes = new ArrayList<>();
    private Collection<Form> connectionAttributes = new ArrayList<>();
    private Collection<Form> connectionGroupAttributes = new ArrayList<>();
    private ConnectionGroup rootConnectionGroup = new AnyhowGuacamoleConnectionGroup();


    public void setSelf(User self) {
        this.self = self;
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public User self() {
        return this.self;
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return this.authenticationProvider;
    }

    @Override
    public Directory<User> getUserDirectory() {
        return userDirectory;
    }

    public void setUserDirectory(Directory<User> userDirectory) {
        this.userDirectory = userDirectory;
    }

    @Override
    public Directory<UserGroup> getUserGroupDirectory() {
        return userGroupDirectory;
    }

    public void setUserGroupDirectory(Directory<UserGroup> userGroupDirectory) {
        this.userGroupDirectory = userGroupDirectory;
    }

    @Override
    public Directory<Connection> getConnectionDirectory() {
        return connectionDirectory;
    }

    public void setConnectionDirectory(Directory<Connection> connectionDirectory) {
        this.connectionDirectory = connectionDirectory;
    }

    @Override
    public Directory<ConnectionGroup> getConnectionGroupDirectory() {
        return connectionGroupDirectory;
    }

    public void setConnectionGroupDirectory(Directory<ConnectionGroup> connectionGroupDirectory) {
        this.connectionGroupDirectory = connectionGroupDirectory;
    }

    @Override
    public Collection<Form> getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(Collection<Form> userAttributes) {
        this.userAttributes = userAttributes;
    }

    @Override
    public Collection<Form> getUserGroupAttributes() {
        return userGroupAttributes;
    }

    public void setUserGroupAttributes(Collection<Form> userGroupAttributes) {
        this.userGroupAttributes = userGroupAttributes;
    }

    @Override
    public Collection<Form> getConnectionAttributes() {
        return connectionAttributes;
    }

    public void setConnectionAttributes(Collection<Form> connectionAttributes) {
        this.connectionAttributes = connectionAttributes;
    }

    @Override
    public Collection<Form> getConnectionGroupAttributes() {
        return connectionGroupAttributes;
    }

    public void setConnectionGroupAttributes(Collection<Form> connectionGroupAttributes) {
        this.connectionGroupAttributes = connectionGroupAttributes;
    }


    @Override
    public ConnectionGroup getRootConnectionGroup() throws GuacamoleException {
        return this.rootConnectionGroup;
    }

    public void setRootConnectionGroup(ConnectionGroup rootConnectionGroup) {
        this.rootConnectionGroup = rootConnectionGroup;
    }
}
