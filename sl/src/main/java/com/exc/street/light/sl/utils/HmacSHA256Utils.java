package com.exc.street.light.sl.utils;

import com.exc.street.light.resource.utils.HexUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Objects;

public final class HmacSHA256Utils {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private HmacSHA256Utils() {
    }

    /**
     * 计算HmacSHA256摘要
     *
     * @param hexSecretKey 密钥(HEX字符串)
     * @param content      内容
     * @return
     * @throws Exception
     */
    public static byte[] digest(String hexSecretKey, byte[] content) throws Exception {
        return digest(HexUtil.hexStringToBytes(hexSecretKey), content);
    }

    /**
     * 计算HmacSHA256摘要
     *
     * @param secretKey 密钥
     * @param content   内容
     * @return
     * @throws Exception
     */
    public static byte[] digest(byte[] secretKey, byte[] content) throws Exception {
        Objects.requireNonNull(secretKey);
        Objects.requireNonNull(content);

        Mac mac = Mac.getInstance(HMAC_SHA256);
        Key key = new SecretKeySpec(secretKey, HMAC_SHA256);
        mac.init(key);
        return mac.doFinal(content);
    }
}
