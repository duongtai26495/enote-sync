package com.kai.mynote.util;


import com.kai.mynote.assets.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Map<String, String> generateToken(String userName){
        Map<String,Object> claims=new HashMap<>();
        return createAccessAndRefreshToken(claims,userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    private Map<String, String> createAccessAndRefreshToken(Map<String, Object> claims, String userName) {
        String access_token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+AppConstants.ACCESS_TOKEN_EXPIRATION_MS))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        String refresh_token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ AppConstants.REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        Map<String, String> tokens = new HashMap<>();
        tokens.put(AppConstants.ACCESS_TOKEN,access_token);
        tokens.put(AppConstants.REFRESH_TOKEN,refresh_token);
        return tokens;
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(AppConstants.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
