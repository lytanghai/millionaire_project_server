package com.millionaire_project.millionaire_project.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

public class RestTemplateHelper {

    private final RestTemplate restTemplate;

    public RestTemplateHelper() {
        this.restTemplate = new RestTemplate();
        // You can customize timeout here if needed
        // e.g., use HttpComponentsClientHttpRequestFactory with timeout config
    }

    /**
     * Perform GET request with optional headers and query parameters
     *
     * @param url           the base URL
     * @param queryParams   map of query parameters (nullable)
     * @param headers       map of headers (nullable)
     * @param responseClass expected response class
     * @param <T>           response type
     * @return response body parsed as T
     */
    public <T> T doGet(String url, Map<String, String> queryParams, Map<String, String> headers, Class<T> responseClass) {
        URI uri = buildUri(url, queryParams);
        HttpEntity<?> entity = new HttpEntity<>(buildHeaders(headers));

        ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.GET, entity, responseClass);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("GET request failed with status: " + response.getStatusCode());
    }

    /**
     * Perform POST request with optional headers and request body
     *
     * @param url           the URL
     * @param requestBody   request body object (will be serialized as JSON)
     * @param headers       map of headers (nullable)
     * @param responseClass expected response class
     * @param <T>           response type
     * @return response body parsed as T
     */
    public <T> T doPost(String url, Object requestBody, Map<String, String> headers, Class<T> responseClass) {
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, buildHeaders(headers));

        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseClass);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("POST request failed with status: " + response.getStatusCode());
    }

    // Helper to build URI with query params
    private URI buildUri(String url, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }
        return builder.build().encode().toUri();
    }

    // Helper to build HttpHeaders from Map
    private HttpHeaders buildHeaders(Map<String, String> headersMap) {
        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null) {
            headersMap.forEach(headers::set);
        }
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return headers;
    }
}
