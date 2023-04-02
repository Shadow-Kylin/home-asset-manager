package cn.shadowkylin.ham.common;

import cn.shadowkylin.ham.config.JwtProperties;
import cn.shadowkylin.ham.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述 Jwt工具类
 */

@Component
public class JwtTokenUtil {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Environment env;

    /**
     * 生成token
     *
     * @param user
     * @return
     */
    public String genToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getPhone())
                .setExpiration(genExp())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getBase64Secret())
                .compact();
    }

    /**
     * 生成过期时间
     */
    public Date genExp() {
        return new Date(System.currentTimeMillis() + jwtProperties.getTokenValidityInMillSeconds());
    }

    /**
     * 获取token中的id
     */
    public String getUserIdFromTokenPayload(String payload) {
        //Claims用来存放payload中的各种信息
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getBase64Secret()).parseClaimsJws(payload).getBody();
        return (String) claims.get("id");
    }

    /**
     * 获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getBase64Secret())
                    .parseClaimsJws(token).getBody();
            return claims.getExpiration();
        }catch (ExpiredJwtException e){
            return e.getClaims().getExpiration();
        }
    }

    /**
     * 验证token是否过期
     */
    public boolean isTokenExpired(String token) {
        // 获取token的过期时间（exp）
        long expiration = getExpirationDateFromToken(token).getTime();
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 比较过期时间和当前时间
        return now > expiration;
    }

    /**
     * 从token中获取用户id
     */
    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getBase64Secret())
                .parseClaimsJws(token).getBody();
        return (int)claims.get("id");
    }

    /**
     * 从token中获取用户手机号
     */
    public String getPhoneFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getBase64Secret())
                .parseClaimsJws(token).getBody();
        return (String)claims.getSubject();
    }

    /**
     * 从token中获取其创建时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getBase64Secret())
                .parseClaimsJws(token).getBody();
        return claims.getIssuedAt();
    }

    /**
     * 检查token是否有效
     */
    public boolean validateToken(String token,User user){
        String phone = getPhoneFromToken(token);
        return (phone.equals(user.getPhone()) && !isTokenExpired(token));
    }
}
