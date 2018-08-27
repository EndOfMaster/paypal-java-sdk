package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalRequest;

import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_JSON;

/**
 * @author ZM.Wang
 */
public class PayPalQueryRefundRequest extends PayPalRequest<PayPalQueryRefundResponse> {

    private final String id;

    public PayPalQueryRefundRequest(String id) {
        this.id = id;
    }

    @Override
    protected String getPath() {
        return "/v1/payments/refund/" + id;
    }

    @Override
    protected int getDataType() {
        return REQ_DATA_TYPE_JSON;
    }

    @Override
    public Class<PayPalQueryRefundResponse> responseClass() {
        return PayPalQueryRefundResponse.class;
    }
}
