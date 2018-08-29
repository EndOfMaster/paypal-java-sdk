package com.endofmaster.paypal.trade;

import com.endofmaster.commons.id.IdGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


/**
 * @author ZM.Wang
 */
public class PayPalTransaction {

    @JsonProperty("reference_id")
    private String referenceId;

    private String description;
    private PayPalAmount amount;

    PayPalTransaction(String description, long amount) {
        this.referenceId = IdGenerator.uuid();
        this.description = description;
        this.amount = new PayPalAmount(amount);
    }

    PayPalTransaction() {
    }

    public long buildMoney() {
        BigDecimal money = new BigDecimal(amount.getTotal());
        BigDecimal value = new BigDecimal(100);
        return money.multiply(value).longValue();
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getDescription() {
        return description;
    }

    public PayPalAmount getAmount() {
        return amount;
    }
}
