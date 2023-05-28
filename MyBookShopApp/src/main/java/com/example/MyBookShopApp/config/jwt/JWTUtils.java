package com.example.MyBookShopApp.config.jwt;

import com.example.MyBookShopApp.model.jwt.JwtBlackList;
import com.example.MyBookShopApp.repositories.JwtBlackListRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JWTUtils {

    @Value("${auth.secret}")
    private String secret;
    @Value(value = ("${blacklist.time}"))
    private long blMinutes;
    private final JwtBlackListRepository jwtBlackListRepository;

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, username);
        blacklistJwt(token);
        return token;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String userName) {
        String username = extractSubject(token);
        return username.equals(userName)
                && !isTokenExpired(token)
                && !jwtBlackListRepository.existsByJwtAndTimeBefore(
                token, LocalDateTime.now()
        );
    }

    public void blacklistJwt(String jwtToken) {
        JwtBlackList jwtBlackList = new JwtBlackList();
        jwtBlackList.setJwt(jwtToken);
        jwtBlackList.setTime(LocalDateTime.now().plusMinutes(blMinutes));
        jwtBlackListRepository.save(jwtBlackList);
    }
}
