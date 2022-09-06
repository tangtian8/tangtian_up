package com.imooc.seller.sign;

import com.imooc.seller.service.SignService;
import com.imooc.util.RSAUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 验签aop
 */
@Component
@Aspect
public class SignAop {

    @Autowired
    private SignService signService;

    @Before(value = "execution(* com.imooc.seller.controller.*.*(..)) && args(authId,sign,text,..)")
    public void verify(String authId,String sign,SignText text){
        String publicKey = signService.getPublicKey(authId);
        Assert.isTrue(RSAUtil.verify(text.toText(),sign,publicKey),"验签失败");
    }
}
