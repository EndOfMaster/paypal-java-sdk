package com.endofmaster.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ZM.Wang
 */
public abstract class PayPalResponse {

    @JsonProperty("message")
    private String error;

    @JsonProperty("error_description")
    private String message;

    public boolean success() {
        return StringUtils.isBlank(error) && StringUtils.isBlank(message);
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}