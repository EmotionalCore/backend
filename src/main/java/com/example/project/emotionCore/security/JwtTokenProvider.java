package com.example.project.emotionCore.security;

import com.example.project.emotionCore.Service.CustomMemberDetail;
import com.example.project.emotionCore.Service.CustomUserDetailService;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.MemberDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final CustomUserDetailService customUserDetailService;

    // access 토큰 만료 30분
    final static int accessTokenExpiration = (30 * 60 * 1000);

    // refresh 토큰 만료 1주일
    final static int refreshTokenExpiration = (7 * 24 * 60 * 60 * 1000);


    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey
    , CustomUserDetailService customUserDetailService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailService = customUserDetailService;
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public JwtTokenDTO generateToken(Authentication authentication) {
        // 권한 가져오기
        CustomMemberDetail member = (CustomMemberDetail) authentication.getPrincipal();
        return generateToken(member);
    }

    public JwtTokenDTO generateToken(CustomMemberDetail member) {
        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + accessTokenExpiration);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("username", member.getUsername())
                .claim("email", member.getEmail())
                .claim("auth", member.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public JwtTokenDTO refreshToken(String refreshToken) {
        if (isValidRefreshToken(refreshToken)) {
            return generateToken(getAuthentication(refreshToken));
        }
        return null;
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        // JWT에서 사용자 Id를 subject로 추출
        long id = Long.parseLong(claims.getSubject());
        UserDetails userDetails = customUserDetailService.loadUserById(id);
        // 인증 객체 생성: Authentication 객체는 보통 UsernamePasswordAuthenticationToken을 사용
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            //log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            //log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            //log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            //log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public boolean isValidRefreshToken(String token) {
        if (token == null || token.isEmpty() || !validateToken(token)) {
            return false;
        }
        Claims claims = parseClaims(token);
        System.out.println(claims.get("email"));
        //email 검사하는건 access token 줄까봐인데 이게 맞는 방식인지는 모름
        if(claims.get("email") != null || claims.getExpiration().before(new Date())){
            return false;
        }
        return true;
    }
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}