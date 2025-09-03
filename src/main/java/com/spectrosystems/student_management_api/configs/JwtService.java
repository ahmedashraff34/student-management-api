package com.spectrosystems.student_management_api.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for generating, validating, and extracting information from JWT tokens.
 * Handles token creation, expiration checks, and claim extraction.
 */
@Service
public class JwtService {

    private final long jwtExpiration = 1000 * 60 * 60; // 1 hour
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token JWT token
     * @return username contained in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     *
     * @param token          JWT token
     * @param claimsResolver function to extract a specific claim
     * @param <T>            type of the claim
     * @return the value of the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user without extra claims.
     *
     * @param userDetails user details
     * @return generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for the given user with extra claims.
     *
     * @param extraClaims additional claims to include in the token
     * @param userDetails user details
     * @return generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Builds the JWT token with the specified claims, subject, and expiration time.
     *
     * @param extraClaims additional claims to include
     * @param userDetails user details
     * @param expiration  expiration time in milliseconds
     * @return JWT token as a string
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(key).compact();
    }

    /**
     * Checks if the provided JWT token is valid for the given user.
     *
     * @param token       JWT token
     * @param userDetails user details
     * @return true if the token is valid, false otherwise
     */
    boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the token has expired.
     *
     * @param token JWT token
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token JWT token
     * @return expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token JWT token
     * @return all claims contained in the token
     */
    private Claims extractAllClaims(String token) {
        // Parse the JWT token to extract claims
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
