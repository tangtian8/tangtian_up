package com.imooc.seller.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 签名服务
 */
@Service
public class SignService {
    static Map<String,String> PUBLIC_KEYS = new HashMap<>();
    static {
        PUBLIC_KEYS.put("1000","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDovNC1bzzZ3lu//xLUIud8a8Fu\n" +
                "bvgXVxFu7Z3jbBjc3thot8gIrpZt951MkdfcUK091kHiDmwiAnMkDJvZI1Y9cWcF\n" +
                "gyKjczR1iDusUTPMGwHkligBx4ocVyoREz8mC0JliSnn8OKhutvnegyFsDI5lVuV\n" +
                "ZyFQPGbzvXtYIJ+cBwIDAQAB");
    }

    /**
     * 根据授权编号获取公钥
     * @param authId
     * @return
     */
    public String getPublicKey(String authId){
        return PUBLIC_KEYS.get(authId);
    }
}
