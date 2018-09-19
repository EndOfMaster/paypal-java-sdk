package com.endofmaster.paypal.trade;

import com.endofmaster.paypal.PayPalResponse;

import java.util.List;

/**
 * @author ZM.Wang
 */
public class PayPalOrderExecuteResponse extends PayPalResponse {

    private String id;

    private List<PayPalTransaction> transactions;

    public String getSaleId() {
        if (transactions.size() > 0) {
            return transactions.get(0).getSaleId();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public PayPalOrderExecuteResponse setId(String id) {
        this.id = id;
        return this;
    }

    public List<PayPalTransaction> getTransactions() {
        return transactions;
    }

    public PayPalOrderExecuteResponse setTransactions(List<PayPalTransaction> transactions) {
        this.transactions = transactions;
        return this;
    }
}
