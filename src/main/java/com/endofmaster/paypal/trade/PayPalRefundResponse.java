package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalResponse;

/**
 * @author ZM.Wang
 */
public class PayPalRefundResponse extends PayPalResponse {

    private String id;

    private String state;

    public String getId() {
        return id;
    }

    public PayPalRefundResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getState() {
        return state;
    }

    public PayPalRefundResponse setState(String state) {
        this.state = state;
        return this;
    }
}
