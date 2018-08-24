package com.endofmaster.paypal.base;

import com.endofmaster.paypal.PayPalResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author ZM.Wang
 */

public class GetAccessTokenResponse extends PayPalResponse {

    private String scope;
    private String nonce;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("expires_in")
    private int expiresIn;

    public String getScope() {
        return scope;
    }

    public String getNonce() {
        return nonce;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAppId() {
        return appId;
    }

    public int getExpiresIn() {
        return expiresIn;
    }


}
