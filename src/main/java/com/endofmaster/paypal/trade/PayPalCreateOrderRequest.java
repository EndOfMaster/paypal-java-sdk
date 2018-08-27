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
        PayPalPurchaseUnit palPurchaseUnit = new PayPalPurchaseUnit(description, amount);
        params.put("purchase_units", Collections.singleton(palPurchaseUnit));
        Map<String, String> urls = new HashMap<>(2);
        urls.put("return_url", returnUrl);
        urls.put("cancel_url", cancelUrl);
        params.put("redirect_urls", urls);
        return params;
    }

    @Override
    protected Map<String, String> buildHeader() {
        Map<String, String> headers = super.buildHeader();
        headers.put("PayPal-Partner-Attribution-Id", "EXAMPLE_MP");
        return headers;
    }

    @Override
    protected String getPath() {
        return "/v1/checkout/orders";
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
