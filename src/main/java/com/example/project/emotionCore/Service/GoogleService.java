package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleService {

    private final Environment env;
    private final MemberService memberService;
    private final RestTemplate restTemplate = new RestTemplate();

    public GoogleService(Environment env, MemberService memberService) {
        this.env = env;
        this.memberService = memberService;
    }

    public JwtTokenDTO socialLogin(String code) {
        String accessToken = getAccessToken(code);
        JsonNode userResourceNode = getUserResource(accessToken);
        String email = userResourceNode.get("email").asText();
        String username = userResourceNode.get("name").asText(); // Google의 경우 name을 username으로 사용

        // 이메일을 통해 사용자 로그인 또는 회원가입 처리
        if (memberService.isEmailRegistered(email)) {
            // 이미 이메일이 존재하면 로그인
            return memberService.signInWithSocial(email); // username 추가
        } else {
            // 이메일이 존재하지 않으면 회원가입
            return memberService.signUpWithSocial(email, username); // username 추가
        }
    }


    private String getAccessToken(String authorizationCode) {
        String clientId = env.getProperty("oauth2.google.client-id");
        String clientSecret = env.getProperty("oauth2.google.client-secret");
        String redirectUri = env.getProperty("oauth2.google.redirect-uri");
        String tokenUri = env.getProperty("oauth2.google.token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
    private JsonNode getUserResource(String accessToken) {
        String resourceUri = env.getProperty("oauth2.google.resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}
