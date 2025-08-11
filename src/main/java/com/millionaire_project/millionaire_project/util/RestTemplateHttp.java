package com.millionaire_project.millionaire_project.util;

import org.apache.hc.client5.http.ConnectTimeoutException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.CompletableFuture;

public class RestTemplateHttp {
    private final Logger log = LoggerFactory.getLogger(RestTemplateHttp.class);

    public static CompletableFuture<String> asyncPostSendRequest(String url,
                                                                 HttpMethod method,
                                                                 String requestBody,
                                                                 MultiValueMap<String, String> headers,
                                                                 int timeoutMillis,
                                                                 boolean isIgnoreSSL) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return RestTemplateHttp.sendRequest(
                        url,
                        method,
                        requestBody,
                        headers,
                        timeoutMillis,
                        isIgnoreSSL);
            } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                throw new RuntimeException("POST request failed: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Convenience method to send HTTP requests without query parameters, returning String response.
     */
    public static String sendRequest(
            String url,
            HttpMethod method,
            String requestBody,
            MultiValueMap<String, String> headers,
            int timeoutMillis,
            boolean isIgnoreSSL
    )throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException  {
        return sendRequest(url, method, requestBody, headers, null, timeoutMillis, isIgnoreSSL, String.class);
    }

    /**
     * Convenience method to send HTTP requests with query parameters, returning String response.
     */
    public static String sendRequest(
            String url,
            HttpMethod method,
            String requestBody,
            MultiValueMap<String, String> headers,
            MultiValueMap<String, String> queryParams,
            int timeoutMillis,
            boolean isIgnoreSSL
    ) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return sendRequest(url, method, requestBody, headers, queryParams, timeoutMillis, isIgnoreSSL, String.class);
    }

    /**
     * Core method to send HTTP requests with optional query params and generic response type.
     */
    public static <T> T sendRequest(
            String url,
            HttpMethod method,
            String requestBody,
            MultiValueMap<String, String> headers,
            MultiValueMap<String, String> queryParams,
            int timeoutMillis,
            boolean isIgnoreSSL,
            Class<T> responseType
    ) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        String fullUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(queryParams != null ? queryParams : new LinkedMultiValueMap<>())
                .toUriString();

        RestTemplate restTemplate = new RestTemplate(RestTemplateHttp.getClientHttpRequestFactory(timeoutMillis, isIgnoreSSL));

        HttpEntity<String> entity = new HttpEntity<>(requestBody, new HttpHeaders(headers));

        try {
            ResponseEntity<T> response = restTemplate.exchange(fullUrl, method, entity, responseType);
            return response.getBody();
        } catch (RestClientException e ) {
            handleRestClientException(e);
            return null;
        }
    }
    /**
     * Handles RestClientException logging for common timeout errors.
     */
    private static void handleRestClientException(RestClientException restClientException) {
        Throwable rootCause = restClientException.getRootCause();
        if (rootCause instanceof SocketTimeoutException) {
            System.out.println("Socket Timeout " + restClientException);
        } else if (rootCause instanceof ConnectTimeoutException) {
            System.out.println("Connect Timeout" + restClientException );
        } else {
            System.out.println("RestClientException occurred" + restClientException);
        }
    }

    public static ClientHttpRequestFactory getClientHttpRequestFactory(int timeoutMillis, boolean isIgnoreSSL)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeoutMillis)
                .setConnectionRequestTimeout(timeoutMillis)
                .setSocketTimeout(timeoutMillis)
                .build();

        CloseableHttpClient httpClient;

        if (isIgnoreSSL) {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(
                    sslContext, NoopHostnameVerifier.INSTANCE);

            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .setDefaultRequestConfig(config)
                    .build();
        } else {
            httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(config)
                    .build();
        }

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}