package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalOrderState;
import com.endofmaster.paypal.PayPalResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author ZM.Wang
 */
public class PayPalCreateOrderResponse extends PayPalResponse {

    private String id;
    private PayPalOrderState status;
    @JsonProperty("purchase_units")
    private List<PayPalPurchaseUnit> purchaseUnits;
    private List<Link> links;

    public String getPayUrl() {
        return links.get(1).getHref();
    }

    private static class Link {
        String href;
        String rel;
        String method;

        public String getHref() {
            return href;
        }

        public String getRel() {
            return rel;
        }

        public String getMethod() {
            return method;
        }
    }

    public String getId() {
        return id;
    }

    public PayPalOrderState getStatus() {
        return status;
    }

    public List<PayPalPurchaseUnit> getPurchaseUnits() {
        return purchaseUnits;
    }

    public List<Link> getLinks() {
        return links;
    }
}
