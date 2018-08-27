package com.endofmaster.paypal.trade;

import com.endofmaster.commons.id.IdGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * @author ZM.Wang
 */
public class PayPalPurchaseUnit {

    @JsonProperty("reference_id")
    private String referenceId;

    private String description;
    private Amount amount;

    PayPalPurchaseUnit(String description, long amount) {
        this.referenceId = IdGenerator.uuid();
        this.description = description;
        this.amount = new Amount(amount / 100.0 + "");
    }

    PayPalPurchaseUnit() {
    }

    private static class Amount {
        String total;
        String currency;

        Amount(String total) {
            this.total = total;
            this.currency = "USD";
        }

        Amount() {
        }

        public String getTotal() {
            return total;
        }

        public String getCurrency() {
            return currency;
        }
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getDescription() {
        return description;
    }

    public Amount getAmount() {
        return amount;
    }
}
