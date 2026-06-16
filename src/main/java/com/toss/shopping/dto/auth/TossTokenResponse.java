package com.toss.shopping.dto.auth;

import com.google.gson.annotations.SerializedName;

public class TossTokenResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("scope")
    private String scope;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return "TossTokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", scope='" + scope + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
