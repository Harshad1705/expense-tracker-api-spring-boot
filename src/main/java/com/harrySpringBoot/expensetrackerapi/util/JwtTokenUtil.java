// package com.harrySpringBoot.expensetrackerapi.util;

// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

// @Component
// public class JwtTokenUtil {

//     private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

//     @Value("${jwt.secret}")
//     private String secret;
    

//     public String generateToken(UserDetails userDetails) {
//         Map<String, Object> claims = new HashMap<>();

//         return Jwts.builder()
//         .setClaims(claims)
//         .setSubject(userDetails.getUsername())
//         .setIssuedAt(new Date(System.currentTimeMillis()))
//         .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512 ,secret )
//                 .compact();
//     }

    
// }


package com.harrySpringBoot.expensetrackerapi.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Base64;

@Component
public class JwtTokenUtil {

    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    private Key signingKey;

    @PostConstruct
    public void init() {
        if (secret.length() < 64) {
            System.err.println("Provided JWT secret key is too short for HS512. Generating a secure key.");
            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        } else {
            signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        // final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        // return claimsResolver.apply(claims);

        final Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String userName = getUserNameFromToken(jwtToken);

        return userName.equals(userDetails.getUsername())&& !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        final Date expiration = getExpirationFromToken(jwtToken);
        return expiration.before(new Date());
    }

    private Date getExpirationFromToken(String jwtToken) {
       return  getClaimFromToken(jwtToken, Claims::getExpiration);
    }
}
