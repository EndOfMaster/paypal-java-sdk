package com.endofmaster.paypal.trade;

/**
 * @author ZM.Wang
 */
public class PayPalAmount {

    private String total;
    private String currency;

    PayPalAmount(long total) {
        this.total = String.format("%.2f", total / 100.0);
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
