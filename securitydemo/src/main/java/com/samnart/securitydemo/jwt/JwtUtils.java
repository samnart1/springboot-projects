package com.samnart.securitydemo.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirations}")
    private int jwtExpirations;

    public String getJwtFromHeader(HttpServletRequest request) {
        String BearerToken = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", BearerToken);
        if (BearerToken != null && BearerToken.startsWith("Bearer ")) {
            return BearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(((new Date()).getTime()) + jwtExpirations) )
            .signWith(key())
            .compact();
    }

    // public String generateTokenFromUsername(UserDetails userDetails) {
    //     String username = userDetails.getUsername();
    //     return Jwts.builder()
    //         .subject(username)
    //         .issuedAt(new Date())
    //         .expiration(new Date((new Date()).getTime() + jwtExpirations))
    //         .signWith(key())
    //         .compact();
    // }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
            .verifyWith((SecretKey) key())
            .build().parseSignedClaims(token)
            .getPayload().getSubject();
        }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }



}



































































// package com.samnart.securitydemo.jwt;

// import io.jsonwebtoken.*;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import jakarta.servlet.http.HttpServletRequest;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;

// import javax.crypto.SecretKey;
// import java.security.Key;
// import java.util.Date;

// @Component
// public class JwtUtils {
//     private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

//     @Value("${spring.app.jwtSecret}")
//     private String jwtSecret;

//     @Value("${spring.app.jwtExpirations}")
//     private int jwtExpirationMs;

//     public String getJwtFromHeader(HttpServletRequest request) {
//         String bearerToken = request.getHeader("Authorization");
//         logger.debug("Authorization Header: {}", bearerToken);
//         if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//             return bearerToken.substring(7);
//         }
//         return null;
//     }

//     public String generateTokenFromUsername(UserDetails userDetails) {
//         String username = userDetails.getUsername();
//         return Jwts.builder()
//             .subject(username)
//             .issuedAt(new Date())
//             .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
//             .signWith(key())
//             .compact();
//     }

//     public String getUserNameFromJwtToken(String token) {
//         return Jwts.parser()
//             .verifyWith((SecretKey) key())
//             .build().parseSignedClaims(token)
//             .getPayload().getSubject();
//     }

//     private Key key() {
//         return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//     }

//     public boolean validateJwtToken(String authToken) {
//         try {
//             System.out.println("Validate");
//             Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
//             return true;
//         } catch (MalformedJwtException e) {
//             logger.error("Invalid JWT token: {}", e.getMessage());
//         } catch (ExpiredJwtException e) {
//             logger.error("JWT token is expired: {}", e.getMessage());
//         } catch (UnsupportedJwtException e) {
//             logger.error("JWT token is unsupported: {}", e.getMessage());
//         } catch (IllegalArgumentException e) {
//             logger.error("JWT claims string is empty: {}", e.getMessage());
//         }
        
//         return false;

//     }


// }

