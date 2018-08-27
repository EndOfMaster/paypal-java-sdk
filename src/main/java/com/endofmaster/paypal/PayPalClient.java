package com.endofmaster.paypal;

import com.endofmaster.commons.util.StreamUtils;
import com.endofmaster.paypal.base.GetAccessTokenRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.endofmaster.paypal.PayPalConstant.CHARSET;
import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_FORM;
import static com.endofmaster.paypal.PayPalConstant.REQ_DATA_TYPE_JSON;


/**
 * @author ZM.Wang
 */
public class PayPalClient {

    private static final Logger logger = LoggerFactory.getLogger(PayPalClient.class);
    private final static ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String BASE_URL;
    private final static String PROD_BASE_URL = "https://api.paypal.com";
    private final static String TEST_BASE_URL = "https://api.sandbox.paypal.com";

    private final String authorization;
    private final HttpClient httpClient;

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
        } catch (UnsupportedEncodingException e) {
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
                    requestBuilder = RequestBuilder.get(BASE_URL + request.getPath() + request.pathParam());
                    logger.debug("请求PayPal发送参数：" + request.pathParam());
                }
            }
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
            HttpResponse response = httpClient.execute(requestBuilder.build());
            String json = StreamUtils.copyToString(response.getEntity().getContent(), Charset.forName(CHARSET));
            logger.debug("PayPal请求结果json：" + json);
            return MAPPER.readValue(json, request.responseClass());
        } catch (IOException e) {
            throw new PayPalException(e);
        }
    }
}