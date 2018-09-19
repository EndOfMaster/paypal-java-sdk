package com.endofmaster.paypal.trade;

/**
 * @author ZM.Wang
 */
public class PayPalRelatedResources {

    private Sale sale;

    static class Sale {
        private String id;

        public String getId() {
            return id;
        }

        public Sale setId(String id) {
            this.id = id;
            return this;
        }
    }

    public Sale getSale() {
        return sale;
    }

}
