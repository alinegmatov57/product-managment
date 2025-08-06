package security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET_KEY = "3crt346ef14937c1c0ea519f8fc123a80fcd04a7420f8e8bcd0a7567c272e007b";
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30 minut

    public static String generateAccessToken(User user) {
        return generateToken(user, EXPIRATION_TIME, SECRET_KEY);
    }

    public static   String generateToken(User user, Long expirationTime, String secretKey) {
        return Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(secretKey))
                .compact();
    }


    private static SecretKey getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String getIdFromAccessToken(String token) {
        return getUsernameFromToken(token, SECRET_KEY);
    }

    private String getUsernameFromToken(String token, String secretKey) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}