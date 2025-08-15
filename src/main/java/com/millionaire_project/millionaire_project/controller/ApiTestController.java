package com.millionaire_project.millionaire_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.dto.res.DynamicResponse;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class ApiTestController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseBuilder<DynamicResponse> proxyRequest(@RequestBody Map<String, Object> request) {
        try {
            // Extract fields
            String url = (String) request.get("url");
            String method = (String) request.getOrDefault("method", "POST");
            Map<String, String> headers = (Map<String, String>) request.get("headers");
            Object body = request.get("body");

            // Build headers
            HttpHeaders httpHeaders = new HttpHeaders();
            if (headers != null) {
                headers.forEach(httpHeaders::add);
            }
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            // Build request entity
            HttpEntity<Object> entity = body != null ? new HttpEntity<>(body, httpHeaders) : new HttpEntity<>(httpHeaders);

            // Determine HTTP method
            HttpMethod httpMethod = HttpMethod.resolve(method.toUpperCase());
            if (httpMethod == null) httpMethod = HttpMethod.POST;

            // Send request
            ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, entity, String.class);

            // Convert JSON string into DynamicResponse
            ObjectMapper mapper = new ObjectMapper();
            DynamicResponse dynamicResponse = mapper.readValue(response.getBody(), DynamicResponse.class);

            return ResponseBuilder.success(dynamicResponse);

        } catch (Exception e) {
            return null;
        }
    }

}
