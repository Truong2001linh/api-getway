package com.do_an.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  public String extractUnique(String token) {
    return extractClaim(token,Claims::getSubject);
  }
  public Integer extractIdUser(String token){
    return (Integer) extractClaim(token,"idUser");
  }
  public String extractRole(String token){
    return (String) extractClaim(token,"role");
  }
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }
  public Object extractClaim(String token,String claim){
    final Claims claims = extractAllClaims(token);
    return  claims.get(claim);
  }

//  public boolean isTokenValid(String token, UserDetails userDetails) {
//    final String username = extractUnique(token);
//    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
