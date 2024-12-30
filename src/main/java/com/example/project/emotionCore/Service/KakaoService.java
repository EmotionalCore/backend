package com.example.project.emotionCore.Service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {

    private final Environment env;
    private final MemberService memberService;
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoService(Environment env, MemberService memberService) {
        this.env = env;
        this.memberService = memberService;
    }

    public void socialLogin(String code) {
        String accessToken = getAccessToken(code);
        JsonNode userResourceNode = getUserResource(accessToken);
        String userId = userResourceNode.get("id").asText()+"@local_kakao.com";
        String username = userResourceNode.get("properties").get("nickname").asText();

        if (memberService.isEmailRegistered(userId)) {
            // 이미 이메일이 존재하면 로그인
            memberService.signInWithSocial(userId); // username 추가
        } else {
            // 이메일이 존재하지 않으면 회원가입
            memberService.signUpWithSocial(userId, username); // username 추가
        }

    }

    private String getAccessToken(String authorizationCode) {
        String clientId = env.getProperty("spring.security.oauth2.client.registration.kakao.client-id");
        String clientSecret = env.getProperty("spring.security.oauth2.client.registration.kakao.client-secret");
        String redirectUri = env.getProperty("spring.security.oauth2.client.registration.kakao.redirect-uri");
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken) {
        String resourceUri = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class);
        return response.getBody();
    }
}