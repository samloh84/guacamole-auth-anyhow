package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowUserGroup;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.AbstractUserGroup;
import org.apache.guacamole.net.auth.RelatedObjectSet;
import org.apache.guacamole.net.auth.permission.ObjectPermissionSet;
import org.apache.guacamole.net.auth.simple.SimpleObjectPermissionSet;
import org.apache.guacamole.net.auth.simple.SimpleRelatedObjectSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnyhowGuacamoleUserGroup extends AbstractUserGroup {
    private Map<String, String> attributes = new HashMap<>();
    private Set<String> memberUsers = new HashSet<>();
    private Set<String> memberUserGroups = new HashSet<>();
    private Set<String> userGroups = new HashSet<>();


    private Set<String> connectionPermissions;
    private Set<String> connectionGroupPermissions;
    private Set<String> userPermissions;
    private Set<String> userGroupPermissions;


    public AnyhowGuacamoleUserGroup() {
    }

    public AnyhowGuacamoleUserGroup(AnyhowUserGroup userGroup) {
        this();
        this.setIdentifier(userGroup.getIdentifier());
        this.setAttributes(new HashMap<>(userGroup.getAttributes()));
        this.setMemberUsers(new HashSet<>(userGroup.getUsers()));
        this.setMemberUserGroups(new HashSet<>(userGroup.getGroups()));
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
        RelatedObjectSet relatedObjectSet = new SimpleRelatedObjectSet();
        relatedObjectSet.addObjects(userGroups);
        return relatedObjectSet;
    }

    @Override
    public RelatedObjectSet getMemberUsers() throws GuacamoleException {
        RelatedObjectSet relatedObjectSet = new SimpleRelatedObjectSet();
        relatedObjectSet.addObjects(memberUsers);
        return relatedObjectSet;
    }

    @Override
    public RelatedObjectSet getMemberUserGroups() throws GuacamoleException {
        RelatedObjectSet relatedObjectSet = new SimpleRelatedObjectSet();
        relatedObjectSet.addObjects(memberUserGroups);
        return relatedObjectSet;
    }

    public void setMemberUsers(Set<String> memberUsers) {
        this.memberUsers = memberUsers;
    }

    public void setMemberUserGroups(Set<String> memberUserGroups) {
        this.memberUserGroups = memberUserGroups;
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


}
