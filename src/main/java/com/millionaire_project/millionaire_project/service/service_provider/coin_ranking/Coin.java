package com.millionaire_project.millionaire_project.service.service_provider.coin_ranking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.dto.res.DynamicResponse;
import com.millionaire_project.millionaire_project.entity.CredentialEntity;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.service.CredentialService;
import com.millionaire_project.millionaire_project.service.service_provider.coin_paprika_operation.CoinPaprika;
import com.millionaire_project.millionaire_project.util.DateUtil;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import com.millionaire_project.millionaire_project.util.RestTemplateHelper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class Coin {

    private final Logger log = LoggerFactory.getLogger(CoinPaprika.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CredentialService credentialService;

    public ResponseBuilder<ApiResponder> getCoinDetail(String coinUUID, String endpoint, String providerName) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(Static.COIN_RANKING_BASE_URL)
                .path(endpoint.replace(Static.COIN_ID, coinUUID));

        try {

            CredentialEntity credentialEntity = credentialService.findTheMostUsages(providerName);

            Map<String,String> header = new HashMap<>();
            header.put("x-access-token", credentialEntity.getApiKey());

            RestTemplateHelper client = new RestTemplateHelper();
            String result = client.doGet(
                    builder.build().toString(),
                    null,
                    header,
                    String.class);

            log.info("full build request for coin paprika{}", builder.build().encode().toUri());

            credentialService.updateRemaining(credentialEntity);

            if(result.isEmpty()) {
               throw new ServiceException("","");
            }

            JSONObject customResp = new JSONObject();
            JSONObject resultObject = new JSONObject(result);

            if(resultObject != null) {
                JSONObject coinData = resultObject.optJSONObject("data").optJSONObject("coin");
                customResp.put("supply", coinData.optJSONObject("supply"));
                customResp.put("supply_at", DateUtil.convertUnixToDt(coinData.optJSONObject("supply").optLong("supplyAt")));
                customResp.put("num_of_exchanges", coinData.optInt("numberOfExchanges"));
                customResp.put("num_of_markets", coinData.optInt("numberOfMarkets"));
                customResp.put("market_cap", coinData.optString("marketCap"));
                customResp.put("fully_diluted_market_cap", coinData.optString("fullyDilutedMarketCap"));
                customResp.put("price_at", DateUtil.convertUnixToDt(coinData.optLong("priceAt")));
                customResp.put("price", coinData.optString("price"));
                customResp.put("ath_date", DateUtil.convertUnixToDt(coinData.optJSONObject("allTimeHigh").optLong("timestamp")));
                customResp.put("ath_price", coinData.optJSONObject("allTimeHigh").optString("price"));

                ApiResponder resultBuilder = new ApiResponder();

                resultBuilder.setContent(objectMapper.readValue(customResp.toString(), DynamicResponse.class));

                log.info("COIN_RANKING_GET_COIN_DETAIL COMPLETED!");
                return ResponseBuilder.success(resultBuilder);
            }
        } catch(ServiceException | JsonProcessingException e){
            throw new ServiceException(ApplicationCode.ERSP01.getCode(), ApplicationCode.ERSP01.getMessage());
        }

        return null;
    }

}
