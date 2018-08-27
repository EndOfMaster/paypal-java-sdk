package com.endofmaster.paypal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZM.Wang
 */
public abstract class PayPalRequest<T extends PayPalResponse> {

    protected Map<String, Object> buildParams() {
        return new HashMap<>();
    }

    protected Map<String, String> buildHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return headers;
    }

    protected abstract String getPath();

    protected abstract int getDataType();

    public abstract Class<T> responseClass();

    protected String pathParam() {
        return "";
    }

    protected String method() {
        return "POST";
    }

}
