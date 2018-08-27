import com.endofmaster.paypal.PayPalClient;
import com.endofmaster.paypal.RefreshAccessToken;
import com.endofmaster.paypal.base.GetAccessTokenRequest;
import com.endofmaster.paypal.base.GetAccessTokenResponse;
import com.endofmaster.paypal.trade.PayPalCreateOrderRequest;
import com.endofmaster.paypal.trade.PayPalCreateOrderResponse;
import com.endofmaster.paypal.trade.PayPalQueryOrderRequest;
import org.junit.Test;

public class PayPalClientTest {

    private final static String clientId = "AZhCTq6c3OP-swNcHYVvLra347KY82jYT65TyZ7WNlaTjyHYVDrr4GrzkZ2akHwk9xnAZVJJbcRW5cm0";
    private final static String secret = "EDE6TrTOICc-yF3oQEbIXiIPxnp3IC2PEzBfCVVIlJMGBDL_zswNXVDusqCl-_8pB6pWGS1RuAhahWE1";

    private final PayPalClient client;

    public PayPalClientTest() throws InterruptedException {
        this.client = new PayPalClient(clientId, secret, false);
        new RefreshAccessToken(client);
        Thread.sleep(1000);
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
        PayPalCreateOrderRequest request = new PayPalCreateOrderRequest(1, "测试商品：北京一日游", "http://127.0.0.1", "http://127.0.0.1");
        Thread.sleep(2000);
        PayPalCreateOrderResponse response = client.execute(request);
        if (response.success()) {
            System.err.println(response.getPayUrl());
        } else {
            System.err.println(response.getMessage());
        }
    }

    @Test
    public void queryOrder() throws InterruptedException {
        PayPalQueryOrderRequest request = new PayPalQueryOrderRequest("4C199276N3192112D");
        Thread.sleep(2000);
        PayPalCreateOrderResponse response = client.execute(request);
        if (response.success()) {
            System.err.println(response.getStatus());
        } else {
            System.err.println(response.getMessage());
        }
    }

}