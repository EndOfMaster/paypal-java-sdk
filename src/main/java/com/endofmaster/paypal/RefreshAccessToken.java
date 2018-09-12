package com.endofmaster.paypal;

import com.endofmaster.paypal.base.GetAccessTokenRequest;
import com.endofmaster.paypal.base.GetAccessTokenResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ZM.Wang
 * 定时刷新token
 */
public class RefreshAccessToken {

    private final static Logger logger = LoggerFactory.getLogger(RefreshAccessToken.class);

    private Date expiredAt;
    private final ScheduledExecutorService task;

    private final PayPalClient client;
    String accessToken;

    public RefreshAccessToken(PayPalClient client) {
        this.client = client;
        this.task = Executors.newSingleThreadScheduledExecutor();
        start();
    }

    private void start() {
        this.task.scheduleWithFixedDelay(this::refresh, 0, 5 * 60, TimeUnit.SECONDS);
        logger.info("Started PayPalTokenRefresher");
    }

    private void refresh() {
        try {
            if (expired()) {
                GetAccessTokenRequest request = new GetAccessTokenRequest();
                GetAccessTokenResponse response = client.execute(request, null);
                if (response.success()) {
                    this.accessToken = response.getAccessToken();
                    this.expiredAt = DateUtils.addSeconds(new Date(), response.getExpiresIn() - (60 * 10));
                } else {
                    throw new PayPalException("Failed to refresh PayPal token：" + response.getErrorMsg());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to refresh PayPal token", e);
        }
    }

    private boolean expired() {
        logger.debug("Checking if PayPal token has expired");
        boolean expired = StringUtils.isBlank(this.accessToken) || expiredAt.before(new Date());
        logger.debug("PayPal token has expired? " + (expired ? "YES" : "NO"));
        return expired;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
