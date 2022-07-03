package org.fast.spirit.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.fast.spirit.security.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
  private String SECRET_KEY;
  @Value("${fast-spirit.token.duration:86400000}") private Long duration;

  public JwtUtil(@Value("${fast-spirit.token.secret_key:}") String secret_key) {
    if (secret_key.isEmpty()) {
      String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
      this.SECRET_KEY = RandomStringUtils.random(20, characters);
    }
  }
  public String extractIdentification(String token) {
    return extractClaim(token, Claims::getSubject);
  }
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }
  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername());
  }

  public String createTokenForActiveClient(Long id) {
    return Jwts.builder().setClaims(new HashMap<>()).setSubject(id.toString() + ":" + System.nanoTime()).setIssuedAt(new Date(System.currentTimeMillis()))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }
  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + duration))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String identification = extractIdentification(token);
    return identification.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }}
