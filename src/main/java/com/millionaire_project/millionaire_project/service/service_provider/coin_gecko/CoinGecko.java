package com.millionaire_project.millionaire_project.service.service_provider.coin_gecko;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.dto.res.DynamicResponse;
import com.millionaire_project.millionaire_project.entity.CredentialEntity;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.repository.CredentialRepository;
import com.millionaire_project.millionaire_project.service.CredentialService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import com.millionaire_project.millionaire_project.util.RestTemplateHelper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CoinGecko {
    private final Logger log = LoggerFactory.getLogger(CoinGecko.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private CredentialRepository credentialRepository;

    public ResponseBuilder<ApiResponder> getCoinDetail(String coinId, String uri, String providerName) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(Static.COIN_GECKO_CAP_BASE_URL)
                .path(uri.replace(Static.COIN_ID, coinId));

        try {
            List<CredentialEntity> credentialObject = credentialRepository.findByProviderName(providerName);
            Optional<CredentialEntity> getRemaining = credentialObject.stream()
                    .filter(c -> c.getProviderName().equalsIgnoreCase(providerName))
                    .max(Comparator.comparingInt(CredentialEntity::getRemaining));

            CredentialEntity entity = getRemaining.orElseThrow(() ->
                    new ServiceException(ApplicationCode.W001.getCode(), ApplicationCode.W001.getMessage()));

            Map<String, String> headers = new HashMap<>();
            headers.put(Static.COIN_GECKO_API_HEADER, entity.getApiKey());

            RestTemplateHelper client = new RestTemplateHelper();
            String result = client.doGet(
                    builder.build().encode().toUri().toString(),
                    null,
                    headers,
                    String.class);
//            String result = loadMockResponse();

            log.info("full build request {} {}",  builder.build().encode().toUri(), headers );


            JSONObject resultObject = new JSONObject(result);
            JSONObject custom = new JSONObject();
            custom.put("id", resultObject.getString("id"));
            custom.put("symbol", resultObject.getString("symbol"));
            custom.put("name", resultObject.optString("name"));
            custom.put("hashing_algorithm", resultObject.optString("hashing_algorithm"));
            custom.put("description", resultObject.optJSONObject("description").optString("en"));
            custom.put("country_origin", resultObject.optString("country_origin", null));
            custom.put("listed_date", resultObject.optString("genesis_date"));
            custom.put("sentiment_votes_up_percentage", resultObject.optDouble("sentiment_votes_up_percentage"));
            custom.put("sentiment_votes_down_percentage", resultObject.optDouble("sentiment_votes_down_percentage"));
            custom.put("watchlist_portfolio_users", resultObject.optInt("watchlist_portfolio_users"));
            custom.put("market_cap_rank", resultObject.optInt("market_cap_rank"));
            custom.put("categories", resultObject.optJSONArray("categories"));
            custom.put("developer_data", resultObject.optJSONObject("developer_data"));
            custom.put("whitepaper", resultObject.optJSONObject("links").optString("whitepaper"));

            JSONObject marketData = resultObject.optJSONObject("market_data");
            if(marketData != null) {
                custom.put("total_volume", marketData.optJSONObject("total_volume").optLong("usd"));
                custom.put("ath_date", marketData.optJSONObject("ath_date").optString("usd"));
                custom.put("ath_price", marketData.optJSONObject("ath").optString("usd"));
                custom.put("atl_date", marketData.optJSONObject("atl_date").optString("usd"));
                custom.put("atl_price", marketData.optJSONObject("atl").optString("usd"));
                custom.put("market_cap", marketData.optJSONObject("market_cap").optLong("usd"));
                custom.put("circulating_supply", marketData.optBigDecimal("circulating_supply", null));
                custom.put("total_supply", marketData.optBigDecimal("total_supply", null));
                custom.put("fully_diluted_valuation", marketData.optJSONObject("fully_diluted_valuation").optString("usd"));
                custom.put("max_supply", marketData.optBigDecimal("max_supply", null));
                custom.put("market_cap_fdv_ratio", marketData.optBigDecimal("market_cap_fdv_ratio", null));
                custom.put("current_price", marketData.optJSONObject("current_price").optString("usd"));
                custom.put("price_change_percentage_in_1h", marketData.optJSONObject("price_change_percentage_1h_in_currency").optString("usd"));
                custom.put("price_change_percentage_in_30d", marketData.optJSONObject("price_change_percentage_30d_in_currency").optString("usd"));
                custom.put("price_change_percentage_in_1y", marketData.optJSONObject("price_change_percentage_1y_in_currency").optString("usd"));
                custom.put("max_supply_infinite", marketData.optBoolean("max_supply_infinite", false));
            }

            ApiResponder resultBuilder = new ApiResponder();

            resultBuilder.setContent(objectMapper.readValue(custom.toString(), DynamicResponse.class));

            credentialService.consume(providerName);
            return ResponseBuilder.success(resultBuilder);

        } catch(ServiceException e){
            throw new ServiceException(ApplicationCode.ERSP01.getCode(), ApplicationCode.ERSP01.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String loadMockResponse() throws Exception {
        return new String(
                Files.readAllBytes(Paths.get(getClass().getClassLoader()
                        .getResource("static/mockresponse.json")
                        .toURI()))
        );
    }
}
