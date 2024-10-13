package com.leski.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Verification;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Set;

@Service
@Slf4j
public class JwtService {
    @Value("${api.jwtSigningKey}")
    private String jwtSigningKey;

    public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        if(claims == null){
            return null;
        }
        return claims.getSubject();
    }
    public String extractRole(String token){
        Claims claims = extractAllClaims(token);
        if(claims == null)
            return null;
        Set<String> aud = claims.getAudience();
        if(aud == null)
            return null;
        return claims.getAudience().stream().findFirst().orElse(null);
    }

    public boolean isTokenValid(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSigningKey().getEncoded());
            Verification verification = JWT.require(algorithm);
            verification.build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            return false;
        }
    }
    public Claims extractAllClaims(String token) {
        try{
            return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        }catch (SignatureException | MalformedJwtException ex){
            log.error(ex.getMessage());
            return null;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}