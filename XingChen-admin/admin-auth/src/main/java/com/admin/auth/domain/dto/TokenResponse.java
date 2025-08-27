package com.admin.auth.domain.dto;

import java.time.Instant;
import java.util.Map;

/**
 * 令牌响应DTO
 * 
 * <p>OAuth 2.1标准令牌响应格式</p>
 * 
 * @author admin
 * @version 1.0
 * @since 2024-01-01
 */
public class TokenResponse {
    
    /**
     * 访问令牌
     */
    private String accessToken;
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
    
    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";
    
    /**
     * 过期时间（秒）
     */
    private Long expiresIn;
    
    /**
     * 作用域
     */
    private String scope;
    
    /**
     * 是否活跃（用于令牌自省）
     */
    private Boolean active;
    
    /**
     * 客户端ID
     */
    private String clientId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 主题
     */
    private String subject;
    
    /**
     * 颁发时间
     */
    private Instant issuedAt;
    
    /**
     * 过期时间
     */
    private Instant expiresAt;
    
    /**
     * 扩展属性
     */
    private Map<String, Object> additionalProperties;

    // 构造方法
    public TokenResponse() {}

    public TokenResponse(String accessToken, String refreshToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    // Getter和Setter方法
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    // Builder模式
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final TokenResponse response = new TokenResponse();

        public Builder accessToken(String accessToken) {
            response.setAccessToken(accessToken);
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            response.setRefreshToken(refreshToken);
            return this;
        }

        public Builder tokenType(String tokenType) {
            response.setTokenType(tokenType);
            return this;
        }

        public Builder expiresIn(Long expiresIn) {
            response.setExpiresIn(expiresIn);
            return this;
        }

        public Builder scope(String scope) {
            response.setScope(scope);
            return this;
        }

        public Builder active(Boolean active) {
            response.setActive(active);
            return this;
        }

        public Builder clientId(String clientId) {
            response.setClientId(clientId);
            return this;
        }

        public Builder username(String username) {
            response.setUsername(username);
            return this;
        }

        public Builder subject(String subject) {
            response.setSubject(subject);
            return this;
        }

        public Builder issuedAt(Instant issuedAt) {
            response.setIssuedAt(issuedAt);
            return this;
        }

        public Builder expiresAt(Instant expiresAt) {
            response.setExpiresAt(expiresAt);
            return this;
        }

        public Builder additionalProperties(Map<String, Object> additionalProperties) {
            response.setAdditionalProperties(additionalProperties);
            return this;
        }

        public TokenResponse build() {
            return response;
        }
    }
}
