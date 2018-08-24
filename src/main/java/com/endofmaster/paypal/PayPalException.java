package com.endofmaster.paypal;

/**
 * @author ZM.Wang
 */
public class PayPalException extends RuntimeException {

    public PayPalException(String errorMsg) {
        super(errorMsg);
    }

    public PayPalException(Throwable e) {
        super(e);
    }

    public PayPalException(String error, Throwable e) {
        super(error, e);
    }
}
