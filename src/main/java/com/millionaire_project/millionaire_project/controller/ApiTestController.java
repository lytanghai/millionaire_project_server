package com.millionaire_project.millionaire_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.dto.res.DynamicResponse;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class ApiTestController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    @GetMapping
    public ResponseBuilder<DynamicResponse> proxyRequest(
            @RequestBody(required = false) Map<String, Object> requestBody,
            @RequestParam Map<String, String> queryParams) {
        try {
            // Decide source of request
            Map<String, Object> request = requestBody != null ? requestBody : new HashMap<>(queryParams);

            // Extract fields
            String url = (String) request.get("url");
            String method = (String) request.getOrDefault("method", "GET");
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
            if (httpMethod == null) httpMethod = HttpMethod.GET;

            // Send request
            ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, entity, String.class);

            // Convert JSON string into DynamicResponse
            ObjectMapper mapper = new ObjectMapper();
            DynamicResponse dynamicResponse = mapper.readValue(response.getBody(), DynamicResponse.class);

            return ResponseBuilder.success(dynamicResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
