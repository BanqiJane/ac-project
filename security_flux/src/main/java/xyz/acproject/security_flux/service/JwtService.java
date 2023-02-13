package xyz.acproject.security_flux.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.acproject.security_flux.entity.UserDetail;
import xyz.acproject.utils.JodaTimeUtils;
import xyz.acproject.utils.SpringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * @author zjian
 * @version JwtUtils v1.0
 */
@Service
public class JwtService {
    private String secret;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUuid(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("uuid").toString();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // TODO 自动生成的方法存根
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        // TODO 自动生成的方法存根
        return Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();
    }

    public List<GrantedAuthority> authorities(String token) {
        final Claims claims = extractAllClaims(token);
        List<String> rolesMap = claims.get("role", List.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rolesMap)) {
            for (String rolemap : rolesMap) {
                authorities.add(new SimpleGrantedAuthority(rolemap));
            }
        }
        return authorities;
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetail userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (StringUtils.isNotBlank(userDetails.getUuid())) claims.put("uuid", userDetails.getUuid());
        if (userDetails.getLevel() != null) claims.put("level", userDetails.getLevel());
//		if(StringUtils.isNotBlank(userDetails.getUsername()))claims.put("name",userDetails.getUsername());

        if (!CollectionUtils.isEmpty(userDetails.getAuthorities())) {
            List<String> authors = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
                authors.add(grantedAuthority.getAuthority());
            }
            claims.put("role", authors);
        }
        return createToken(claims, userDetails.getUsername());
    }


    public String createToken(Map<String, Object> claims, String subject) {
        // TODO 自动生成的方法存根
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(JodaTimeUtils.changeSeconds(new Date(), getExpiredTime()))
                .signWith(SignatureAlgorithm.HS256, getSecret()).compact();
    }


    public Boolean validateToken(String token, UserDetail userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private String getSecret() {
        String key = SpringUtils.getConfigByKey("acproject.security.secret");
        if (StringUtils.isBlank(key) && StringUtils.isBlank(this.secret)) {
            return "acproject.xyz.Inc @ 2020-2021 design by jane!@#$*%^&";
        }
        if (StringUtils.isNotBlank(this.secret)) {
            return this.secret;
        }
        return key;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getExpiredTime() {
        String key = SpringUtils.getConfigByKey("acproject.security.expire");
        if (StringUtils.isBlank(key)) {
            return 60 * 60 * 3;
        }
        return Integer.parseInt(key);
    }
}
