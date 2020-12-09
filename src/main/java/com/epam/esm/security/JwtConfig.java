package com.epam.esm.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String key;
    private String tokenPrefix;
    private Integer daysBeforeExpiration;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public byte[] getKeyAsByteArray() {
        return key.getBytes();
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getDaysBeforeExpiration() {
        return daysBeforeExpiration;
    }

    public void setDaysBeforeExpiration(Integer daysBeforeExpiration) {
        this.daysBeforeExpiration = daysBeforeExpiration;
    }
}
