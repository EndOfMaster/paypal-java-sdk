package com.endofmaster.paypal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZM.Wang
 */
public abstract class PayPalRequest<T extends PayPalResponse> {

    protected Map<String, Object> buildParams() {
        Map<String, Object> params = new HashMap<>();
        return params;
    }

    protected Map<String, String> buildHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return headers;
    }

    protected abstract String getPath();

    protected abstract int getDataType();

    public abstract Class<T> responseClass();

}
