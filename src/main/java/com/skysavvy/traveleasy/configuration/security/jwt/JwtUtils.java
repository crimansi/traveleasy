package com.skysavvy.traveleasy.configuration.security.jwt;

import com.skysavvy.traveleasy.model.user.CostumUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.security.Key;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${skysavvy.app.jwtSecret}")
    private String secret;
    @Value("${skysavvy.app.jwtExpirations}")
    private int expiration;

    public String generateToken(Authentication authentication) {
        CostumUserDetails userDetails = (CostumUserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(key()).build().parse(token);
            return true;
        } catch(MalformedJwtException e) {
            logger.error("Invalid JWT token", e);
        } catch(ExpiredJwtException e) {
            logger.error("Expired JWT token", e);
        } catch(UnsupportedJwtException e) {
            logger.error("Unsupported JWT token", e);
        } catch(IllegalArgumentException e) {
            logger.error("JWT claims string is empty", e);
        }
        return false;
    }
}
