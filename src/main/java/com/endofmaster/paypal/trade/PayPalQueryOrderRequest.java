package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalRequest;

import java.util.Map;

import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_JSON;

/**
 * @author ZM.Wang
 */
public class PayPalQueryOrderRequest extends PayPalRequest<PayPalCreateOrderResponse> {

    private final String id;

    public PayPalQueryOrderRequest(String id) {
        this.id = id;
    }

    @Override
    protected Map<String, Object> buildParams() {
        Map<String, Object> params = super.buildParams();
        params.put("order_id", id);
        return params;
    }

    @Override
    protected Map<String, String> buildHeader() {
        Map<String, String> headers = super.buildHeader();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    protected String pathParam() {
        return id;
    }

    @Override
    protected String method() {
        return "GET";
    }

    @Override
    protected String getPath() {
        return "/v1/checkout/orders/";
    }

    @Override
    protected int getDataType() {
        return REQ_DATA_TYPE_JSON;
    }

    @Override
    public Class<PayPalCreateOrderResponse> responseClass() {
        return PayPalCreateOrderResponse.class;
    }
}
