package com.endofmaster.paypal;

import com.endofmaster.commons.id.IdGenerator;
import com.endofmaster.paypal.base.GetAccessTokenRequest;
import com.endofmaster.paypal.base.GetAccessTokenResponse;
import com.endofmaster.paypal.trade.PayPalCreateOrderRequest;
import com.endofmaster.paypal.trade.PayPalCreateOrderResponse;
import com.endofmaster.paypal.trade.PayPalQueryOrderRequest;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

public class PayPalClientTest {

    private final static String clientId = "AZhCTq6c3OP-swNcHYVvLra347KY82jYT65TyZ7WNlaTjyHYVDrr4GrzkZ2akHwk9xnAZVJJbcRW5cm0";
    private final static String secret = "EDE6TrTOICc-yF3oQEbIXiIPxnp3IC2PEzBfCVVIlJMGBDL_zswNXVDusqCl-_8pB6pWGS1RuAhahWE1";
//    private final static String clientId = "AehCAuJcd6sCLgmOAozgXbkew9LhkRAEj_RKZWoQxInOu7NEzU6wV4BT_Q8tnf7ozR8s3068a72uoK9i";
//    private final static String secret = "EPCkl5BnevQ93iGq7qXaEfydWB-MfBZWaU5eKZyf9XIUSTpn9O5HKGKLC0Hwmq2_ffvY5uMJklLCHAVz";

    private final PayPalClient client;
    private final RefreshAccessToken refreshAccessToken;

    public PayPalClientTest() {
        this.client = new PayPalClient(clientId, secret, false);
        this.refreshAccessToken = new RefreshAccessToken(client);
//        this.refreshAccessToken.accessToken = "A21AAGNc1gRmOOvjNUEngvlZxlAhNcBsX9T7ytM8wmGp937OwgYLJU8oPgkhoA6ZLxDmK3PF3yAU6BipF6DDVajjH80fCMttA";
    }

    @Test
    public void accessTokenTest() {
        GetAccessTokenRequest request = new GetAccessTokenRequest();
        GetAccessTokenResponse response = client.execute(request, refreshAccessToken.getAccessToken());
        if (response.success()) {
            System.err.println(response.getAccessToken());
        } else {
            System.err.println(response.getMessage());
        }
    }

    @Test
    public void createOrder() throws InterruptedException {
        Thread.sleep(3000);
        PayPalCreateOrderRequest request = new PayPalCreateOrderRequest(100, IdGenerator.objectId(), "测试商品：北京一日游",
                "https://trade.zoomdu.com/paySuccess.html", "https://trade.zoomdu.com/paySuccess.html");
        PayPalCreateOrderResponse response = client.execute(request, refreshAccessToken.getAccessToken());
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
        PayPalCreateOrderResponse response = client.execute(request, refreshAccessToken.getAccessToken());
        if (response.success()) {
            System.err.println(response.getState());
            System.err.println(response.getAmount());
        } else {
            System.err.println(response.getErrorMsg());
        }
    }

    @Test
    public void validateReceivedEvent() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, SignatureException {
        String chain = "MIIILDCCBxSgAwIBAgIQDEd6EXcAnHmtnx4u7tJZlTANBgkqhkiG9w0BAQsFADB1MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3d3cuZGlnaWNlcnQuY29tMTQwMgYDVQQDEytEaWdpQ2VydCBTSEEyIEV4dGVuZGVkIFZhbGlkYXRpb24gU2VydmVyIENBMB4XDTE3MDIxODAwMDAwMFoXDTE5MDQyNjEyMDAwMFowggElMR0wGwYDVQQPDBRQcml2YXRlIE9yZ2FuaXphdGlvbjETMBEGCysGAQQBgjc8AgEDEwJVUzEZMBcGCysGAQQBgjc8AgECEwhEZWxhd2FyZTEQMA4GA1UEBRMHMzAxNDI2NzEWMBQGA1UECRMNMjIxMSBOIDFzdCBTdDEOMAwGA1UEERMFOTUxMzExCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMREwDwYDVQQHEwhTYW4gSm9zZTEVMBMGA1UEChMMUGF5UGFsLCBJbmMuMRgwFgYDVQQLEw9QYXJ0bmVyIFN1cHBvcnQxNDAyBgNVBAMTK21lc3NhZ2V2ZXJpZmljYXRpb25jZXJ0cy5zYW5kYm94LnBheXBhbC5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDTAsLQcHFGG0mvLNAlu3FJ/6QUr1EbUSBYnqF/oRbirzJRWbT3JWpFr2palaFNnfpCwLB/KY+UX3xDLB5Ic38piG6FzRe1TiZcxhdIbOY21OvVVFatXfM62lzwX3RASpbVA1cbCoDBSbgyplSPINVd8Gyf+qtJxbW7M6cVuZae1Nx0mA63SfNnRpBwOvYAUkCruCCjIVLqRuWbRzn0wvayVMWasw047rHhtFmaYBwJ1L4sn5NL+HDQ5wSMb1EE46v/aGGdv7bcHhc4Re0C1pFW032MVb85eRD7OtvD2hZM2O5JwiwHMBXzxEjrIbGCA6be3acmzYKxYLZHYt6KlQDBAgMBAAGjggQEMIIEADAfBgNVHSMEGDAWgBQ901Cl1qCt7vNKYApl0yHU+PjWDzAdBgNVHQ4EFgQUgPw27mYvKgltHUaTXExjogwMhv8wNgYDVR0RBC8wLYIrbWVzc2FnZXZlcmlmaWNhdGlvbmNlcnRzLnNhbmRib3gucGF5cGFsLmNvbTAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMHUGA1UdHwRuMGwwNKAyoDCGLmh0dHA6Ly9jcmwzLmRpZ2ljZXJ0LmNvbS9zaGEyLWV2LXNlcnZlci1nMS5jcmwwNKAyoDCGLmh0dHA6Ly9jcmw0LmRpZ2ljZXJ0LmNvbS9zaGEyLWV2LXNlcnZlci1nMS5jcmwwSwYDVR0gBEQwQjA3BglghkgBhv1sAgEwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQuY29tL0NQUzAHBgVngQwBATCBiAYIKwYBBQUHAQEEfDB6MCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdpY2VydC5jb20wUgYIKwYBBQUHMAKGRmh0dHA6Ly9jYWNlcnRzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydFNIQTJFeHRlbmRlZFZhbGlkYXRpb25TZXJ2ZXJDQS5jcnQwDAYDVR0TAQH/BAIwADCCAfgGCisGAQQB1nkCBAIEggHoBIIB5AHiAHYApLkJkLQYWBSHuxOizGdwCjw1mAT5G9+443fNDsgN3BAAAAFaTqxLiAAABAMARzBFAiEApDFqvEjdtugwxjKvZqJ1Y1A1F1UX9EIvZL5Ar02YSe4CIBEf0EzZiic7u3IzI8BUy0oFKh11VL1YIBAZfIj744o/AHYAVhQGmi/XwuzT9eG9RLI+x0Z2ubyZEVzA75SYVdaJ0N0AAAFaTqxM2wAABAMARzBFAiEAn3lwWc95wltvaOnBaM09VQN9xhHHkwecw5+tdHhSimgCIC1BaJ0RodgykU0ii2QzuzEoR8ci6qF+Pj6j1GE5wvU4AHcA7ku9t3XOYLrhQmkfq+GeZqMPfl+wctiDAMR7iXqo/csAAAFaTqxOhwAABAMASDBGAiEA/+kyQku3N/kwrubEfHSM4h20mZTvrAoRZovA5qwpVLICIQDyOQQu9r6kets5tO0bYDXmaVWypOdS/ocseU11p5vBYAB3ALvZ37wfinG1k5Qjl6qSe0c4V5UKq1LoGpCWZDaOHtGFAAABWk6sTEUAAAQDAEgwRgIhALIDDduHsT0LymHxdrOWzzEaDLhVSl5ivHAgWBcdl5AAAiEAgIYoXQHtSAiPyH4METNmAZAhiwo9uwTavdrjLsFJ2nowDQYJKoZIhvcNAQELBQADggEBAMxgCr0MTAbu7aE8ryfK3q8grN4rJOzBBzh8tSKYW4PUFhAc6ZaW2/PNV+H8oYOP3nB28neglNjR/XZCyd7IRJoxSfwzUZhOBAzTFP0sp5CuGZN24j2SrxcKCaq3mszLmwgiRzuDUnRFdI2OHep6EcDelfURGz8w6eTaLw7m3FZrgJSmmAOntirVZeVPLou74ykxWUm0kEi/OxNQWlgAlAHUcGZHpcupddK/dNRmCFSwRBeiGwaLsz2SXYNvTs4it8cisAxTwBD1iqyXEKRSx6HpsmIOznwENimrR8Cz3jtzQJmshPGmEbrpkKaJ7/DJO5EuMKFF6jYKjKiORugFKfU=";
        byte[] data = Base64.decodeBase64(chain);
        client.validateReceivedEvent(new ByteArrayInputStream(data));
    }

}