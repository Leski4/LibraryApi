package com.leski.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    @Value("${api.jwtSigningKey}")
    private String jwtSigningKey;

    public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }
    public String extractRoles(String token){
        Claims claims = extractAllClaims(token);
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
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode("koCQYlY5kyH1lM5hBKleheWGgu4MMm1ADDwXBb8dnN0");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}