package com.endofmaster.paypal;

import com.endofmaster.paypal.PayPalClient;
import com.endofmaster.paypal.RefreshAccessToken;
import com.endofmaster.paypal.base.GetAccessTokenRequest;
import com.endofmaster.paypal.base.GetAccessTokenResponse;
import com.endofmaster.paypal.trade.PayPalCreateOrderRequest;
import com.endofmaster.paypal.trade.PayPalCreateOrderResponse;
import com.endofmaster.paypal.trade.PayPalQueryOrderRequest;
import org.junit.Test;

public class PayPalClientTest {

//    private final static String clientId = "AZhCTq6c3OP-swNcHYVvLra347KY82jYT65TyZ7WNlaTjyHYVDrr4GrzkZ2akHwk9xnAZVJJbcRW5cm0";
//    private final static String secret = "EDE6TrTOICc-yF3oQEbIXiIPxnp3IC2PEzBfCVVIlJMGBDL_zswNXVDusqCl-_8pB6pWGS1RuAhahWE1";
    private final static String clientId = "AehCAuJcd6sCLgmOAozgXbkew9LhkRAEj_RKZWoQxInOu7NEzU6wV4BT_Q8tnf7ozR8s3068a72uoK9i";
    private final static String secret = "EIh6jNAfNHo5SwfKH_TvDsANfS0SyVKOpS9rRlwwgLh0KAEox383RkkQZ7gtSq1Hq5TFD7n980swNyrV";

    private final PayPalClient client;

    public PayPalClientTest() {
        this.client = new PayPalClient(clientId, secret, true);
        PayPalClient.ACCESS_TOKEN="A21AAGNc1gRmOOvjNUEngvlZxlAhNcBsX9T7ytM8wmGp937OwgYLJU8oPgkhoA6ZLxDmK3PF3yAU6BipF6DDVajjH80fCMttA";
//        new RefreshAccessToken(client);
    }

    @Test
    public void accessTokenTest() {
        GetAccessTokenRequest request = new GetAccessTokenRequest();
        GetAccessTokenResponse response = client.execute(request);
        if (response.success()) {
            System.err.println(response.getAccessToken());
        } else {
            System.err.println(response.getMessage());
        }
    }

    @Test
    public void createOrder() throws InterruptedException {
        PayPalCreateOrderRequest request = new PayPalCreateOrderRequest(100, "测试商品：北京一日游",
                "https://trade.zoomdu.com/notify/payPal/charge", "https://trade.zoomdu.com/notify/payPal/charge");
        PayPalCreateOrderResponse response = client.execute(request);
        if (response.success()) {
            System.err.println(response.getPayUrl());
        } else {
            System.err.println(response.getErrorMsg());
        }
    }

    @Test
    public void queryOrder() throws InterruptedException {
        PayPalQueryOrderRequest request = new PayPalQueryOrderRequest("PAY-6V03621221845261CLOELAAY");
        Thread.sleep(2000);
        PayPalCreateOrderResponse response = client.execute(request);
        if (response.success()) {
            System.err.println(response.getState());
            System.err.println(response.getAmount());
        } else {
            System.err.println(response.getErrorMsg());
        }
    }

}