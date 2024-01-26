package com.alertbot;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://kauth.kakao.com/oauth/token"; // 대상 URL

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 전송할 데이터
        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", "authorization_code");
        map.put("client_id", "7ea74159c7a47e8845cd28547f145243");
        map.put("redirect_url", "https://localhost:3000");
        map.put("code", "czUDU8YikYftOxg8X96BDK2s3L7doHb3KOlyvFtVPBV9cPAEWLWS6_pO5DkKKiVPAAABjUGveE9Dz1szkZmFRA");

        // HTTP 요청 생성
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // 응답 출력
        System.out.println(response.getBody());
    }
}
