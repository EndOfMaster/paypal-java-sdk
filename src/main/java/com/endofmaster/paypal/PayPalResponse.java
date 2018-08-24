package com.endofmaster.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ZM.Wang
 */
public abstract class PayPalResponse {

    protected String error;

    @JsonProperty("error_description")
    protected String message;

    public boolean success() {
        return StringUtils.isBlank(error);
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}