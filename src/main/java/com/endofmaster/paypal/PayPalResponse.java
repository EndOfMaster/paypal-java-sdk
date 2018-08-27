package com.endofmaster.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ZM.Wang
 */
public abstract class PayPalResponse {

    @JsonProperty("debug_id")
    private String debugId;

    @JsonProperty("error_description")
    private String errorDescription;

    private String message;

    public boolean success() {
        return StringUtils.isBlank(debugId);
    }

    public String getErrorMsg() {
        return errorDescription + "==" + message;
    }

    public String getDebugId() {
        return debugId;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getMessage() {
        return message;
    }
}