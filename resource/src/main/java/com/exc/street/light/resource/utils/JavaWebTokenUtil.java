package com.exc.street.light.resource.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * jwt工具类
 *
 * @author xiezhipeng
 * @date 2020/3/13
 */
public class JavaWebTokenUtil {

    private static Logger log = LoggerFactory.getLogger(JavaWebTokenUtil.class);

    private static final String SECRET = "excSecurity";

    /**
     * 该方法使用HS256算法和Secret:bankgl生成signKey
     *
     * @return
     */
    private static Key getKeyInstance() {
        //We will sign our JavaWebTokenUtil with our ApiKey secret
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    /**
     * 使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
     *
     * @param subject
     * @param phone
     * @param expirationTime
     * @return
     */
    public static String createJavaWebToken(String subject, String phone, Date expirationTime) {
        //生成登录令牌
        return Jwts.builder().setSubject(subject)
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .claim("phone", phone).setIssuedAt(new Date()).setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
    }

    /**
     * 解析Token，同时也能验证Token，当验证失败返回null
     *
     * @param jwt
     * @return
     */
    public static Integer parserJavaWebToken(String jwt) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(jwt).getBody();
            return Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            log.error("json web token verify failed");
            return null;
        }
    }

    /**
     * 解析Token返回staffId，当验证失败返回null
     *
     * @param request
     * @return
     */
    public static Integer parserStaffIdByToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            final Claims claims = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token).getBody();
            System.out.println(claims.get("phone", String.class));
            return Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            log.error("json web token verify failed");
        }
        return null;
    }
}
