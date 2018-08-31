package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_JSON;

/**
 * @author ZM.Wang
 */
public class PayPalCreateOrderRequest extends PayPalRequest<PayPalCreateOrderResponse> {

    /** Cent */
    private final long amount;
    private final String description;
    private final String returnUrl;
    private final String cancelUrl;

    public PayPalCreateOrderRequest(long amount, String description, String returnUrl, String cancelUrl) {
        this.amount = amount;
        this.description = description;
        this.returnUrl = returnUrl;
        this.cancelUrl = cancelUrl;
    }

    @Override
    protected Map<String, Object> buildParams() {
        Map<String, Object> params = super.buildParams();
        params.put("intent", "sale");
        params.put("payer", Collections.singletonMap("payment_method", "paypal"));
        PayPalTransaction transactions = new PayPalTransaction(description, amount);
        params.put("transactions", Collections.singleton(transactions));
        Map<String, String> urls = new HashMap<>(2);
        urls.put("return_url", returnUrl);
        urls.put("cancel_url", cancelUrl);
        params.put("redirect_urls", urls);
        return params;
    }

    @Override
    protected String getPath() {
        return "/v1/payments/payment";
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
