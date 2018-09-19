package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * @author ZM.Wang
 */
public class PayPalQueryRefundResponse extends PayPalResponse {

    private String id;
    private String state;
    @JsonProperty("invoice_number")
    private String invoiceNumber;

    private PayPalAmount amount;

    public long getMoney() {
        String amount = this.amount.getTotal();
        return new BigDecimal(amount).multiply(new BigDecimal(100)).longValue();
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public PayPalAmount getAmount() {
        return amount;
    }
}
