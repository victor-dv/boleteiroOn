package br.com.boleiroOn.config.infra.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtToken {

    @Value("${security.token.secret.user}")
    private String secretKey;

    @Value("${security.token.expiration-hours}")
    private long expirationHours;

    public String generateToken(String login, Long userId, String role) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(login)
                .withClaim("userId", userId)
                .withClaim("role", role)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(expirationHours, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .build()
                    .verify(token); // Retorna o JWT decodificado inteiro
        } catch (JWTVerificationException e) {
            return null;
        }
    }
    public Long extractUserId(String token) {
        try {
            token = token.replace("Bearer ", "").trim();
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt.getClaim("userId").asLong();
        } catch (Exception e) {
            return null;
        }
    }
}