package sg.gov.tech.cet.anyhowguacamoleextension.model.jackson;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnyhowUserGroup {
    private String name;
    private String identifier;
    private Map<String, String> attributes = Collections.emptyMap();

    private List<String> users = Collections.emptyList();
    private List<String> groups = Collections.emptyList();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

}
