package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalRequest;

import java.util.Map;

import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_JSON;

/**
 * @author ZM.Wang
 */
public class PayPalOrderExecuteRequest extends PayPalRequest<PayPalOrderExecuteResponse> {

    private final String id;
    private final String payerId;

    public PayPalOrderExecuteRequest(String id, String payerId) {
        this.id = id;
        this.payerId = payerId;
    }

    @Override
    protected Map<String, Object> buildParams() {
        Map<String, Object> params = super.buildParams();
        params.put("payer_id", payerId);
        return params;
    }

    @Override
    protected String getPath() {
        return "/v1/payments/payment/" + id + "/execute";
    }

    @Override
    protected int getDataType() {
        return REQ_DATA_TYPE_JSON;
    }

    @Override
    public Class<PayPalOrderExecuteResponse> responseClass() {
        return PayPalOrderExecuteResponse.class;
    }
}
