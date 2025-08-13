package com.millionaire_project.millionaire_project.service.service_provider.coin_market_cap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.dto.res.DynamicResponse;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.service.CredentialService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import com.millionaire_project.millionaire_project.util.RestTemplateHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class Market {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CredentialService credentialService;

    public ResponseBuilder<ApiResponder> getMarketPairLatest(String coinId, String uri, String providerName) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(Static.COIN_MARKET_CAP_BASE_URL)
                .path("/market-pairs/latest")
                .queryParam("symbol", coinId.toUpperCase(Locale.ROOT))
                .queryParam("convert", "USD");

        URI fullUri = builder.build().encode().toUri();
        System.out.println("CMC URL: " + fullUri);

        try {
            RestTemplateHelper client = new RestTemplateHelper();

            Map<String, String> headers = new HashMap<>();
            headers.put("X-CMC_PRO_API_KEY", "57cd5930-852b-478f-939e-0c96c37b0016");

            String result = client.doGet(
                    fullUri.toString(),
                    headers,
                    null,
                    String.class
            );

            JSONObject json = new JSONObject(result);
            JSONArray marketPairs = json.getJSONArray("data");

            ApiResponder resultBuilder = new ApiResponder();

            if (marketPairs.length() > 0) {
                JSONObject firstPair = marketPairs.getJSONObject(0);
                DynamicResponse dynamicResponse = objectMapper.readValue(firstPair.toString(), DynamicResponse.class);
                resultBuilder.setContent(dynamicResponse);
                System.out.println(dynamicResponse);
            }

            return ResponseBuilder.success(resultBuilder);
        }catch (ServiceException | JsonProcessingException ex) {
            throw new ServiceException(ApplicationCode.ERSP01.getCode(), ApplicationCode.ERSP01.getMessage());
        }
    }
}
