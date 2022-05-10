package cn.lllllan.springdemo.utils;

import cn.lllllan.springdemo.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT 工具类
 */
public class JWT {

    /**
     * 过期时间
     */
    private static final long EXPIRE = 60000 * 60 * 24 * 7;

    /**
     * 加密密钥
     */
    private static final String SECRET = "cn.lllllan";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "lllllan";

    /**
     * subject
     */
    private static final String SUBJECT = "lllllan";

    /**
     * 根据用户信息，生成令牌
     *
     * @param user
     * @return
     */
    public static String getJWT(User user) {
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("head_img", user.getHeadImg())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        token = TOKEN_PREFIX + token;

        return token;

    }

    /**
     * 校验令牌及是否过期
     *
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }
}
