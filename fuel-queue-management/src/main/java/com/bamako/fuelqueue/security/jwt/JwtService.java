package com.bamako.fuelqueue.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.security.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(properties.getExpirationMinutes(), ChronoUnit.MINUTES);
        return JWT.create()
            .withIssuer(properties.getIssuer())
            .withSubject(user.getId().toString())
            .withClaim("phone", user.getPhone())
            .withClaim("role", user.getRole().name())
            .withIssuedAt(now)
            .withExpiresAt(expiry)
            .sign(getAlgorithm());
    }

    public DecodedJWT validateToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm())
            .withIssuer(properties.getIssuer())
            .build();
        return verifier.verify(token);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(properties.getSecret());
    }
}
