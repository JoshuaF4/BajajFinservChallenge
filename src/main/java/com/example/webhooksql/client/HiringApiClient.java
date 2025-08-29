package com.example.webhooksql.client;

import com.example.webhooksql.dto.WebhookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class HiringApiClient {

    private final RestTemplate rest;

    @Value("${app.endpoints.generate}")
    private String generateUrl;
    @Value("${app.endpoints.submit}")
    private String submitUrl;

    @Value("${app.candidate.name}")
    private String name;
    @Value("${app.candidate.regNo}")
    private String regNo;
    @Value("${app.candidate.email}")
    private String email;

    public HiringApiClient(RestTemplate rest) {
        this.rest = rest;
    }

    public WebhookResponse generateWebhook() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("regNo", regNo);
        body.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        ResponseEntity<WebhookResponse> resp = rest.exchange(
                generateUrl,
                HttpMethod.POST,
                req,
                WebhookResponse.class
        );
        WebhookResponse info = resp.getBody();
        if (info == null || info.getWebhook() == null || info.getAccessToken() == null) {
            throw new IllegalStateException("Invalid response from generateWebhook");
        }
        System.out.println("Received webhook: " + info.getWebhook());
        return info;
    }

    public ResponseEntity<String> submitFinalQuery(WebhookResponse info, String finalSql) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // IMPORTANT: Use returned token exactly as Authorization header value (no Bearer)
        headers.set("Authorization", info.getAccessToken());

        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", finalSql);

        HttpEntity<Map<String, String>> req = new HttpEntity<>(body, headers);
        return rest.exchange(
                submitUrl,
                HttpMethod.POST,
                req,
                String.class
        );
    }
}
