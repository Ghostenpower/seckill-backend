package com.example.seckill_backend.util;

import com.example.seckill_backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JWT {
    private static String signKey="ghostensuger";
    private static Long expire=4320000L;

    public static String CreateJwt(Map<String ,Object> claims){
        String jwt= Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256,signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }


    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    public static Integer getUserId(String jwt){
        Claims claims=parseJWT(jwt);
        return (Integer)claims.get("user_id");
    }

}
