package com.exc.street.light.sl.utils;

import com.exc.street.light.resource.utils.HexUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public final class AESUtils {

    private static final String AES = "AES";

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private AESUtils() {
    }

    /**
     * 加密, ECB模式, PKCS#5填充
     *
     * @param hexSecretKey 密钥(HEX字符串)
     * @param plaintext    明文
     * @return
     * @throws Exception
     */
    public static byte[] encryptECBPKCS5Padding(String hexSecretKey, byte[] plaintext) throws Exception {
        return encryptECBPKCS5Padding(HexUtil.hexStringToBytes(hexSecretKey), plaintext);
    }

    /**
     * 加密, ECB模式, PKCS#5填充
     *
     * @param secretKey 密钥
     * @param plaintext 明文
     * @return
     * @throws Exception
     */
    public static byte[] encryptECBPKCS5Padding(byte[] secretKey, byte[] plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        Key key = new SecretKeySpec(secretKey, AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    /**
     * 解密, ECB模式, PKCS#5填充
     *
     * @param hexSecretKey 密钥(HEX字符串)
     * @param ciphertext   密文
     * @return
     * @throws Exception
     */
    public static byte[] decryptECBPKCS5Padding(String hexSecretKey, byte[] ciphertext) throws Exception {
        return decryptECBPKCS5Padding(HexUtil.hexStringToBytes(hexSecretKey), ciphertext);
    }

    /**
     * 解密, ECB模式, PKCS#5填充
     *
     * @param secretKey  密钥
     * @param ciphertext 密文
     * @return
     * @throws Exception
     */
    public static byte[] decryptECBPKCS5Padding(byte[] secretKey, byte[] ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        Key key = new SecretKeySpec(secretKey, AES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(ciphertext);
    }
}