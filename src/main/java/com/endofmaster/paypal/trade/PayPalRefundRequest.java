package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalRequest;

import java.util.Map;

import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_JSON;

public class PayPalRefundRequest extends PayPalRequest<PayPalRefundResponse> {

    private final String id;
    private final long amount;

    public PayPalRefundRequest(String id, long amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    protected Map<String, Object> buildParams() {
        Map<String, Object> params = super.buildParams();
        params.put("amount", new PayPalAmount(amount));
        return params;
    }

    @Override
    protected String getPath() {
        return "/v1/payments/sale/" + id + "/refund";
    }

    @Override
    protected int getDataType() {
        return REQ_DATA_TYPE_JSON;
    }

    @Override
    public Class<PayPalRefundResponse> responseClass() {
        return PayPalRefundResponse.class;
    }
}
