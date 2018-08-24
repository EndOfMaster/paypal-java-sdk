package com.endofmaster.paypal.base;

import com.endofmaster.paypal.PayPalRequest;

import java.util.Map;

import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_FORM;

/**
 * @author ZM.Wang
 */
public class GetAccessTokenRequest extends PayPalRequest<GetAccessTokenResponse> {

    @Override
    protected Map<String, Object> buildParams() {
        Map<String, Object> params = super.buildParams();
        params.put("grant_type", "client_credentials");
        return params;
    }

    @Override
    protected Map<String, String> buildHeader() {
        Map<String, String> headers = super.buildHeader();
        headers.put("Accept-Language", "en_US");
        return headers;
    }

    @Override
    protected String getPath() {
        return "/v1/oauth2/token";
    }

    @Override
    protected int getDataType() {
        return REQ_DATA_TYPE_FORM;
    }

    @Override
    public Class<GetAccessTokenResponse> responseClass() {
        return GetAccessTokenResponse.class;
    }
}
