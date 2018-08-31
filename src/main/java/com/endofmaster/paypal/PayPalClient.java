package com.endofmaster.paypal;

import com.endofmaster.commons.util.StreamUtils;
import com.endofmaster.commons.util.crypto.CipherUtils;
import com.endofmaster.commons.util.validate.ParamUtils;
import com.endofmaster.paypal.base.GetAccessTokenRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.endofmaster.paypal.PayPalConstant.CHARSET;
import static com.endofmaster.paypal.PayPalConstant.HEADER_CERT_URL;
import static com.endofmaster.paypal.PayPalConstant.HEADER_TRANSMISSION_ID;
import static com.endofmaster.paypal.PayPalConstant.HEADER_TRANSMISSION_SIG;
import static com.endofmaster.paypal.PayPalConstant.HEADER_TRANSMISSION_TIME;
import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_FORM;
import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_JSON;
import static com.endofmaster.paypal.PayPalConstant.TRUST_CERT;
import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * @author ZM.Wang
 */
public class PayPalClient {

    private final static Logger logger = LoggerFactory.getLogger(PayPalClient.class);
    private final static ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final static String PROD_BASE_URL = "https://api.paypal.com";
    private final static String TEST_BASE_URL = "https://api.sandbox.paypal.com";

    private final String BASE_URL;
    private final String authorization;
    private final HttpClient httpClient;
    private final CertificateFactory certFactory;
    private final X509Certificate trustCert;

    static volatile String ACCESS_TOKEN;

    public PayPalClient(String clientId, String secret, boolean isProd) {
        try {
            this.BASE_URL = isProd ? PROD_BASE_URL : TEST_BASE_URL;
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(60000)
                    .setConnectTimeout(30000)
                    .build();
            this.httpClient = HttpClientBuilder.create()
                    .setMaxConnTotal(200)
                    .setMaxConnPerRoute(100)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            byte[] encoded = Base64.encodeBase64((clientId + ":" + secret).getBytes(CHARSET));
            this.authorization = new String(encoded, CHARSET);
            this.certFactory = CertificateFactory.getInstance("X.509");
            this.trustCert = (X509Certificate) certFactory.generateCertificates(new ByteArrayInputStream(Base64.decodeBase64(TRUST_CERT))).toArray()[0];
        } catch (UnsupportedEncodingException | CertificateException e) {
            throw new PayPalException("初始化PayPalClient异常", e);
        }
    }

    public <T extends PayPalResponse> T execute(PayPalRequest<T> request) {
        try {
            Map<String, String> headers = request.buildHeader();
            if (request instanceof GetAccessTokenRequest) {
                headers.put("Authorization", "Basic " + authorization);
            } else {
                headers.put("Authorization", "Bearer " + ACCESS_TOKEN);
            }
            RequestBuilder requestBuilder = RequestBuilder.post();
            Map<String, Object> params = request.buildParams();
            if (request.getDataType() == REQ_DATA_TYPE_FORM) {
                requestBuilder.setUri(BASE_URL + request.getPath());
                logger.debug("请求PayPal发送参数：" + params);
                List<NameValuePair> formParams = params.keySet().stream().map(mapKey -> new BasicNameValuePair(mapKey, params.get(mapKey).toString())).collect(Collectors.toList());
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, CHARSET);
                requestBuilder.setEntity(entity);
            }
            if (request.getDataType() == REQ_DATA_TYPE_JSON) {
                if ("POST".equalsIgnoreCase(request.method())) {
                    requestBuilder.setUri(BASE_URL + request.getPath());
                    String reqJson = MAPPER.writeValueAsString(params);
                    logger.debug("请求PayPal发送参数：" + reqJson);
                    StringEntity entity = new StringEntity(reqJson, CHARSET);
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    requestBuilder.setEntity(entity);
                } else {
                    requestBuilder = RequestBuilder.get(BASE_URL + request.getPath());
                    logger.debug("请求PayPal发送参数：" + request.getPath());
                }
            }
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
            HttpResponse response = httpClient.execute(requestBuilder.build());
            String json = StreamUtils.copyToString(response.getEntity().getContent(), UTF_8);
            logger.debug("PayPal请求结果json：" + json);
            return MAPPER.readValue(json, request.responseClass());
        } catch (IOException e) {
            throw new PayPalException(e);
        }
    }

    /** 完整验签流程 */
    public boolean validateSign(HttpServletRequest request, String webHookId) {
        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            Map<String, String> headers = new HashMap<>();
            while (headerNames.hasMoreElements()) {
                String nextElement = headerNames.nextElement();
                headers.put(nextElement, request.getHeader(nextElement));
            }
            String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName(CHARSET));
            String clientChainUrl = ParamUtils.findParam(headers, HEADER_CERT_URL);
            String transmissionId = ParamUtils.findParam(headers, HEADER_TRANSMISSION_ID);
            String transmissionTime = ParamUtils.findParam(headers, HEADER_TRANSMISSION_TIME);
            String transmissionSig = ParamUtils.findParam(headers, HEADER_TRANSMISSION_SIG);
            HttpResponse response = httpClient.execute(new HttpGet(clientChainUrl));
            InputStream clientChainStream = response.getEntity().getContent();
            if (clientChainStream != null) {
                X509Certificate clientChain = validateReceivedEvent(clientChainStream);
                return verify(webHookId, transmissionSig, transmissionId, transmissionTime, body, clientChain.getPublicKey());
            } else {
                return false;
            }
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | InvalidKeyException | SignatureException e) {
            throw new PayPalException("验签错误", e);
        }
    }

    /** 用子证书验签 */
    private boolean verify(String webHookId, String transmissionSig, String transmissionId, String transmissionTime, String body, PublicKey clientChain) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String expectedSignature = transmissionId + "|" + transmissionTime + "|" + webHookId + "|" + CipherUtils.crc32(body);
        Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
        signatureAlgorithm.initVerify(clientChain);
        signatureAlgorithm.update(expectedSignature.getBytes());
        byte[] actualSignature = Base64.decodeBase64(transmissionSig.getBytes());
        return signatureAlgorithm.verify(actualSignature);
    }

    /**
     * 验证子证书是否合法
     * @param clientChainStream 子证书
     */
    private X509Certificate validateReceivedEvent(InputStream clientChainStream) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        X509Certificate clientChain = (X509Certificate) certFactory.generateCertificates(clientChainStream).toArray()[0];

        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(null, "".toCharArray());
        keyStore.setCertificateEntry("payPalCert", trustCert);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        X509TrustManager trustManagers = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];

        trustManagers.checkClientTrusted(new X509Certificate[]{clientChain}, "RSA");
        clientChain.checkValidity();
        return clientChain;
    }

}