package io.github.leocalheiros.imageliteapi.middlewares.jwt;

import io.github.leocalheiros.imageliteapi.domain.AccessToken;
import io.github.leocalheiros.imageliteapi.domain.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKeyGenerator secretKeyGenerator;

    public AccessToken generateToken(User user){

        var key = secretKeyGenerator.getKey();
        var expirationDate = generateExpirationDate();
        var claims = generateTokenClaims(user);

        String token = Jwts
                .builder()
                .signWith(key)
                .subject(user.getEmail())
                .expiration(expirationDate)
                .claims(claims)
                .compact();
        return new AccessToken(token);
    }

    private Date generateExpirationDate(){
        var expirationMinutes = 60;
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        return claims;
    }

    public String getEmailFromToken(String tokenJWT){
        try {
            return Jwts.parser().verifyWith(secretKeyGenerator.getKey())
                    .build()
                    .parseSignedClaims(tokenJWT)
                    .getPayload()
                    .getSubject();
        } catch(JwtException e){
            throw new InvalidTokenException(e.getMessage());
        }
    }
}
