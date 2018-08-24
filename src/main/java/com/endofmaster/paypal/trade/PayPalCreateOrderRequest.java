package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_JSON;

/**
 * @author ZM.Wang
 */
public class PayPalCreateOrderRequest extends PayPalRequest<PayPalCreateOrderResponse> {

    private final double amount;
    private final String name;
    private final String description;
    private final String returnUrl;
    private final String cancelUrl;

    public PayPalCreateOrderRequest(long amount, String name, String description, String returnUrl, String cancelUrl) {
        this.amount = amount;
        this.name = name;
        this.description = description;
        this.returnUrl = returnUrl;
        this.cancelUrl = cancelUrl;
    }

    @Override
    protected Map<String, Object> buildParams() {
        Map<String, Object> purchaseUnits = new HashMap<>();
        purchaseUnits.put("reference_id", UUID.randomUUID().toString().replace("-", ""));
        purchaseUnits.put("description", description);
        Map<String, String> amount = new HashMap<>(2);
        amount.put("total", this.amount / 100.0 + "");
        amount.put("currency", "USD");
        purchaseUnits.put("amount", amount);
        Items items = new Items(name, description, this.amount / 100.0 + "", "1");
        purchaseUnits.put("items", Collections.singletonList(items));
        Map<String, Object> params = super.buildParams();
        params.put("purchase_units", Collections.singleton(purchaseUnits));
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

    private static class Items {
        String name;
        String description;
        String price;
        String quantity;
        String currency;

        Items(String name, String description, String price, String quantity) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.currency = "USD";
        }

        Items() {
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getPrice() {
            return price;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getCurrency() {
            return currency;
        }
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
