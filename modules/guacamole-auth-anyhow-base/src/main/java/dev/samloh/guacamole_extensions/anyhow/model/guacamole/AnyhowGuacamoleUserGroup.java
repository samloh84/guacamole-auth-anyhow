package dev.samloh.guacamole_extensions.anyhow.model.guacamole;

import dev.samloh.guacamole_extensions.anyhow.model.jackson.AnyhowUserGroup;
import org.apache.guacamole.net.auth.AbstractUserGroup;

import java.util.*;

public class AnyhowGuacamoleUserGroup extends AbstractUserGroup {
    private AnyhowUserGroup userGroup;

    public AnyhowGuacamoleUserGroup(AnyhowUserGroup userGroup) {
        this.userGroup = userGroup;
    }


    @Override
    public String getIdentifier() {
        return userGroup.getIdentifier();
    }

    @Override
    public void setIdentifier(String identifier) {
        userGroup.setIdentifier(identifier);
    }

    public void setMemberUsers(Set<String> memberUsers) {
        userGroup.setUsers(new ArrayList<>(memberUsers));
    }

    public Set<String> getMemberGroups() {
        return new HashSet<>(userGroup.getGroups());
    }

    public void setMemberGroups(Set<String> memberGroups) {
        userGroup.setGroups(new ArrayList<>(memberGroups));
    }

    public Set<String> getParentGroups() {
        return Collections.emptySet();
    }

    public void setParentGroups(Set<String> parentGroups) {
        return;
    }


    @Override
    public Map<String, String> getAttributes() {
        return userGroup.getAttributes();
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        userGroup.setAttributes(attributes);
    }


    public AnyhowUserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(AnyhowUserGroup userGroup) {
        this.userGroup = userGroup;
    }


}
