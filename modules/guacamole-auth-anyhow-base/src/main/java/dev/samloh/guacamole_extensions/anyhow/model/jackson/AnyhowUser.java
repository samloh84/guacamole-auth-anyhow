package dev.samloh.guacamole_extensions.anyhow.model.jackson;

import dev.samloh.guacamole_extensions.anyhow.util.CidrUtil;
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.net.auth.Credentials;

import java.util.List;
import java.util.Map;

public class AnyhowUser {
    private String name;
    private String identifier;
    private Map<String, String> attributes;

    private String passwordHash;
    private String password;

    private List<String> cidrs;

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getCidrs() {
        return cidrs;
    }

    public void setCidrs(List<String> cidrs) {
        this.cidrs = cidrs;
    }

    public boolean validateCredentials(Credentials credentials) {

        if (StringUtils.isNotBlank(this.identifier)) {
            if (StringUtils.isBlank(credentials.getUsername())) {
                return false;
            }
            if (!credentials.getUsername().equals(this.identifier)) {
                return false;
            }
        }

        if (StringUtils.isNotBlank(this.passwordHash)) {
            if (StringUtils.isBlank(credentials.getPassword())) {
                return false;
            }
            if (!this.passwordHash.equals(Crypt.crypt(credentials.getPassword(), this.passwordHash))) {
                return false;
            }
        }

        if (StringUtils.isNotBlank(this.password)) {
            if (StringUtils.isBlank(credentials.getPassword())) {
                return false;
            }
            if (!credentials.getPassword().equals(this.password)) {
                return false;
            }
        }


        if (cidrs != null && !cidrs.isEmpty()) {
            if (StringUtils.isBlank(credentials.getRemoteAddress())) {
                return false;
            }
            if (!CidrUtil.checkIpAddress(credentials.getRemoteAddress(), cidrs)) {
                return false;
            }
        }

        return true;
    }
}
