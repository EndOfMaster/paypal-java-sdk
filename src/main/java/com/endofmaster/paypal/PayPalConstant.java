package com.endofmaster.paypal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author ZM.Wang
 */
public interface PayPalConstant {
    String CHARSET = "UTF-8";
    DateFormat REQ_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    DateFormat PAID_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    int REQ_DATA_TYPE_JSON = 0;
    int REQ_DATA_TYPE_FORM = 1;
    int REQ_DATA_TYPE_MULTIPART_FORM = 2;
}
