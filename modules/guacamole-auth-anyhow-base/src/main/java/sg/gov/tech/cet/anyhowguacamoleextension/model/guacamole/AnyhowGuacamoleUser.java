package sg.gov.tech.cet.anyhowguacamoleextension.model.guacamole;

import sg.gov.tech.cet.anyhowguacamoleextension.model.jackson.AnyhowUser;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.AbstractUser;
import org.apache.guacamole.net.auth.Permissions;
import org.apache.guacamole.net.auth.RelatedObjectSet;
import org.apache.guacamole.net.auth.permission.ObjectPermissionSet;
import org.apache.guacamole.net.auth.permission.SystemPermissionSet;
import org.apache.guacamole.net.auth.simple.SimpleObjectPermissionSet;
import org.apache.guacamole.net.auth.simple.SimpleRelatedObjectSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnyhowGuacamoleUser extends AbstractUser {

    private Map<String, String> attributes = new HashMap<>();
    private Set<String> userGroups = new HashSet<>();

    private Set<String> connectionPermissions = new HashSet<>();
    private Set<String> connectionGroupPermissions = new HashSet<>();
    private Set<String> userPermissions = new HashSet<>();
    private Set<String> userGroupPermissions = new HashSet<>();

    private Set<String> effectiveConnectionPermissions = new HashSet<>();
    private Set<String> effectiveConnectionGroupPermissions = new HashSet<>();
    private Set<String> effectiveUserPermissions = new HashSet<>();
    private Set<String> effectiveUserGroupPermissions = new HashSet<>();


    public AnyhowGuacamoleUser() {
    }

    public AnyhowGuacamoleUser(AnyhowUser user) {
        this.setIdentifier(user.getIdentifier());
        this.setAttributes(new HashMap<>(user.getAttributes()));
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public RelatedObjectSet getUserGroups() throws GuacamoleException {
        return new SimpleRelatedObjectSet(userGroups);
    }

    public void setUserGroups(Set<String> userGroups) {
        this.userGroups = userGroups;
    }

    @Override
    public ObjectPermissionSet getConnectionPermissions() throws GuacamoleException {
        return new SimpleObjectPermissionSet(connectionPermissions);
    }

    @Override
    public ObjectPermissionSet getConnectionGroupPermissions() throws GuacamoleException {
        return new SimpleObjectPermissionSet(connectionGroupPermissions);
    }

    @Override
    public ObjectPermissionSet getUserPermissions() throws GuacamoleException {
        return new SimpleObjectPermissionSet(userPermissions);
    }

    @Override
    public ObjectPermissionSet getUserGroupPermissions() throws GuacamoleException {
        return new SimpleObjectPermissionSet(userGroupPermissions);
    }


    @Override
    public Permissions getEffectivePermissions() throws GuacamoleException {
        return new Permissions() {
            @Override
            public ObjectPermissionSet getActiveConnectionPermissions() throws GuacamoleException {
                return ObjectPermissionSet.EMPTY_SET;
            }

            @Override
            public ObjectPermissionSet getConnectionGroupPermissions() throws GuacamoleException {
                return new SimpleObjectPermissionSet(effectiveConnectionGroupPermissions);
            }

            @Override
            public ObjectPermissionSet getConnectionPermissions() throws GuacamoleException {
                return new SimpleObjectPermissionSet(effectiveConnectionPermissions);
            }

            @Override
            public ObjectPermissionSet getSharingProfilePermissions() throws GuacamoleException {
                return ObjectPermissionSet.EMPTY_SET;
            }

            @Override
            public SystemPermissionSet getSystemPermissions() throws GuacamoleException {
                return SystemPermissionSet.EMPTY_SET;
            }

            @Override
            public ObjectPermissionSet getUserPermissions() throws GuacamoleException {
                return new SimpleObjectPermissionSet(effectiveUserPermissions);
            }

            @Override
            public ObjectPermissionSet getUserGroupPermissions() throws GuacamoleException {
                return new SimpleObjectPermissionSet(effectiveUserGroupPermissions);
            }
        };
    }

    public void setConnectionPermissions(Set<String> connectionPermissions) {
        this.connectionPermissions = connectionPermissions;
    }

    public void setConnectionGroupPermissions(Set<String> connectionGroupPermissions) {
        this.connectionGroupPermissions = connectionGroupPermissions;
    }

    public void setUserPermissions(Set<String> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public void setUserGroupPermissions(Set<String> userGroupPermissions) {
        this.userGroupPermissions = userGroupPermissions;
    }

    public void setEffectiveConnectionPermissions(Set<String> effectiveConnectionPermissions) {
        this.effectiveConnectionPermissions = effectiveConnectionPermissions;
    }

    public void setEffectiveConnectionGroupPermissions(Set<String> effectiveConnectionGroupPermissions) {
        this.effectiveConnectionGroupPermissions = effectiveConnectionGroupPermissions;
    }

    public void setEffectiveUserPermissions(Set<String> effectiveUserPermissions) {
        this.effectiveUserPermissions = effectiveUserPermissions;
    }

    public void setEffectiveUserGroupPermissions(Set<String> effectiveUserGroupPermissions) {
        this.effectiveUserGroupPermissions = effectiveUserGroupPermissions;
    }
}
