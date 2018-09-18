package com.endofmaster.paypal.trade;

/**
 * @author ZM.Wang
 */
public class PayPalItems {

    private String name;
    private String quantity;
    private String price;
    private String currency;

    public PayPalItems(String name, long price) {
        this.name = name;
        this.quantity = "1";
        this.price = price / 100.0 + "";
        this.currency = "USD";
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
