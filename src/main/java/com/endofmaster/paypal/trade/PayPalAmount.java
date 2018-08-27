package com.endofmaster.paypal.trade;

/**
 * @author ZM.Wang
 */
public class PayPalAmount {

    private String total;
    private String currency;

    PayPalAmount(long total) {
        this.total = total / 100.0 + "";
        this.currency = "USD";
    }

    PayPalAmount() {
    }

    public String getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }
}
