package com.exc.street.light.sl.utils;

import com.exc.street.light.sl.config.parameter.LoraNewApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public final class SendDataUtils {

    private static final boolean ENCRYPTION = true; // 指定项目是否加密（默认为true）

    private static String SIGN_TOKEN; // 项目的signToken

    private static String SECRET_KEY; // 项目的secretKey

    @Autowired
    public void setLoraNewApi(LoraNewApi loraNewApi) {
        SIGN_TOKEN = loraNewApi.getSendSignToken();
        SECRET_KEY = loraNewApi.getSendSecretKey();
    }

    private SendDataUtils() {
    }

    /**
     * @param payloadData 应用数据载荷
     * @param timestamp   对应timestamp字段
     * @return
     */
    public static String signAndEncrypt(byte[] payloadData, Long timestamp) throws Exception {

        // 1. 获取当前的UNIX时间戳（单位ms，时区取LoRaWAN核心网所在的时区）
        //
        // 2. 计算签名
        //
        // 3. 如项目加密属性为true, 加密之
        //
        // 4. Base64编码

        byte[] signed = HmacSHA256Utils.digest(SIGN_TOKEN,
                ByteUtils.concat(payloadData, ByteUtils.longToBytesBE(timestamp)));

        byte[] plaintext = ByteUtils.concat(signed, payloadData);
        if (ENCRYPTION) {
            plaintext = AESUtils.encryptECBPKCS5Padding(SECRET_KEY, plaintext);
        }
        return Base64.getEncoder().encodeToString(plaintext);
    }
}