package com.endofmaster.paypal.trade;

import com.endofmaster.commons.id.IdGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;


/**
 * @author ZM.Wang
 */
public class PayPalTransaction {

    @JsonProperty("reference_id")
    private String referenceId;
    @JsonProperty("invoice_number")
    private String chargeId;

    private String description;
    private PayPalAmount amount;

    @JsonProperty("item_list")
    private Map<String, Object> itemList;

    PayPalTransaction(String chargeId, String description, long amount) {
        this.referenceId = IdGenerator.uuid();
        this.chargeId = chargeId;
        this.description = description;
        this.amount = new PayPalAmount(amount);
    }

    public PayPalTransaction buildItemList(PayPalItems payPalItems) {
        this.itemList = Collections.singletonMap("items", Collections.singletonList(payPalItems));
        return this;
    }

    public Map<String, Object> getItemList() {
        return itemList;
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

    public String getChargeId() {
        return chargeId;
    }

}
