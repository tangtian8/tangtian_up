package com.imooc.seller;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * 测试节流限速
 */
public class TykRateQuotaLimitTest {
    private static RestTemplate rest = new RestTemplate();
    static Logger LOG = LoggerFactory.getLogger(TykRateQuotaLimitTest.class);

    private static String baseUrl = "http://www.tyk-portal-test.com/seller/product/001?authId=59f5556a69f513000113117942ec3e2b76f74a326a7a7b2bacf8efbd";

    @BeforeClass
    public static void init() {

        ResponseErrorHandler errorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        };
        rest.setErrorHandler(errorHandler);
    }
    @Test
    public void rateLimitTest(){
        int count = 10;
        while (count -- >0){
            Assert.isTrue(isSuccess(),"访问失败");
        }
        Assert.isTrue(!isSuccess(),"限速失败");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.isTrue(isSuccess(),"限速失败");
    }
    @Test
    public void quotasTest(){
        int count = 10;
        while (count -- >0){
            Assert.isTrue(isSuccess(),"访问失败");
        }
        Assert.isTrue(!isSuccess(),"配额限制失败");
    }
    boolean isSuccess(){
        ResponseEntity resp = rest.getForEntity(baseUrl,String.class);
        LOG.info("请求结果:{}",resp.getBody());
        return resp.getStatusCode().is2xxSuccessful();
    }
}
