//***************************************************************************************
//
//     Filename: JwtUtils.java
//     Author: Kyle McColgan
//     Date: 21 November 2024
//     Description: This file contains the auth token object definition.
//
//***************************************************************************************

package com.kmccol1.to_do_app.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

//***************************************************************************************

@Component
public class JwtUtils
{
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(String username,
                                   Collection<? extends GrantedAuthority> authorities)
    {
        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public Claims getClaimsFromToken(String token)
    {
        try
        {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (JwtException | IllegalArgumentException e)
        {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return null;
        }
    }

    public String getUserNameFromJwtToken(String token)
    {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    public boolean validateJwtToken(String authToken)
    {
        return getClaimsFromToken(authToken) != null;
    }
}

//***************************************************************************************