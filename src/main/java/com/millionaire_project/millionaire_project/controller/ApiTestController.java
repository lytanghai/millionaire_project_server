package com.millionaire_project.millionaire_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.dto.req.ApiTesterReq;
import com.millionaire_project.millionaire_project.dto.res.DynamicResponse;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class ApiTestController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/experiment")
    public ResponseBuilder<DynamicResponse> experiment(@RequestBody ApiTesterReq req) throws JsonProcessingException {
        if(req == null) {
            throw new ServiceException("Req Body IS NULL","");
        }

        String url = req.getUrl();
        String httpMethod = req.getMethod().toUpperCase(Locale.ROOT);

        Map<String, Object> headersMap = req.getHeader();
        Map<String, Object> queryParams = req.getQueryParam();
        Map<String, Object> body = req.getBody();

        HttpMethod method = null;
        if(httpMethod.equals("POST")) {
            method = HttpMethod.POST;
        } else if(httpMethod.equals("PATCH")){
            method = HttpMethod.PATCH;
        } else {
            method = HttpMethod.GET;
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        if (queryParams != null) {
            queryParams.forEach((k, v) -> uriBuilder.queryParam(k, v));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (headersMap != null) {
            headersMap.forEach((k, v) -> headers.add(k, v.toString()));
        }

        HttpEntity<Object> entity;
        if (body != null && !body.isEmpty() && (httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("PATCH"))) {
            entity = new HttpEntity<>(body, headers);
        } else {
            entity = new HttpEntity<>(headers);
        }

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                method,
                entity,
                String.class
        );
        // Convert JSON string to DynamicResponse
        ObjectMapper mapper = new ObjectMapper();
        DynamicResponse dynamicResponse = mapper.readValue(response.getBody(), DynamicResponse.class);

        return ResponseBuilder.success(dynamicResponse);
    }

}
