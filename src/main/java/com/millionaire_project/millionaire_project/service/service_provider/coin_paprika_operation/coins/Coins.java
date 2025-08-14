package com.millionaire_project.millionaire_project.service.service_provider.coin_paprika_operation.coins;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.List;

@Service
public class Coins {
    private final Logger log = LoggerFactory.getLogger(Coins.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CredentialService credentialService;

    public ResponseBuilder<ApiResponder> getCoinDetail(String coinId, String uri, String providerName) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(Static.COIN_PAPRIRIKA_BASE_URL)
                .path(uri.replace(Static.COIN_ID, coinId));

        try {

            RestTemplateHelper client = new RestTemplateHelper();
            String result = client.doGet(
                    builder.build().encode().toUri().toString(),
                    null,
                    null,
                    String.class);
            log.info("full build request for coin paprika{}",  builder.build().encode().toUri());

            JSONObject resultObject = new JSONObject(result);

            JSONObject customResponse = new JSONObject();
            customResponse.put("symbol_name", resultObject.optString("name"));
            customResponse.put("symbol", resultObject.optString("symbol"));
            customResponse.put("rank", resultObject.optInt("rank"));
            customResponse.put("is_new", resultObject.optBoolean("is_new"));
            customResponse.put("is_active", resultObject.optBoolean("is_active"));
            customResponse.put("type", resultObject.optString("type"));
            customResponse.put("logo", resultObject.optString("logo"));
            customResponse.put("coin_description", resultObject.optString("description"));
            customResponse.put("started_at", resultObject.optString("started_at"));
            customResponse.put("org_structure", resultObject.optString("org_structure"));
            customResponse.put("hash_algorithm", resultObject.optString("hash_algorithm"));
            customResponse.put("open_source", resultObject.optBoolean("open_source"));
            customResponse.put("team", resultObject.optJSONArray("team"));

            JSONObject links = resultObject.optJSONObject("links", null);
            JSONObject listExplorer = new JSONObject();
            if (links != null) {
                if (links.optJSONArray("explorer", null) != null) {
                    List<Object> explorers  = links.getJSONArray("explorer").toList();
                    for (int i = 0; i < explorers.size(); i++) {
                        String transform = getSiteNameFromUrl(explorers.get(i).toString());
                        listExplorer.put(transform, explorers.get(i));
                    }
                }
            }
            customResponse.put("explorer", listExplorer);

            JSONArray linkExtended = resultObject.optJSONArray("links_extended", null);
            JSONObject listLinkExplorer = new JSONObject();

            if(linkExtended != null) {
                for (int i = 0; i < linkExtended.length(); i++) {
                    JSONObject each = new JSONObject(linkExtended.get(i).toString());
                    String transform = getSiteNameFromUrl(each.getString("url"));
                    listLinkExplorer.put(transform, each.getString("url"));
                }
            }
            customResponse.put("link_extended", listLinkExplorer);

            if (resultObject.optJSONArray("tags", null) != null) {
                for (Object o : resultObject.getJSONArray("tags")) {
                    JSONObject object = (JSONObject) o;
                    if (object.getString("id").contains("layer")) {
                        customResponse.put("id", object.getString("id"));
                        customResponse.put("name", object.getString("name"));
                    }
                }
            }

            ApiResponder resultBuilder = new ApiResponder();

            resultBuilder.setContent(objectMapper.readValue(customResponse.toString(), DynamicResponse.class));

            credentialService.consume(providerName);
            log.info("COIN_PAPRIRIKA_BASE_URL COMPLETED!");
            return ResponseBuilder.success(resultBuilder);
        } catch(ServiceException | JsonProcessingException e){
            throw new ServiceException(ApplicationCode.ERSP01.getCode(), ApplicationCode.ERSP01.getMessage());
        }
    }

    public ResponseBuilder<ApiResponder> getTodayOHLC(String coinId, String uri, String providerName) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(Static.COIN_PAPRIRIKA_BASE_URL)
                .path(uri.replace(Static.COIN_ID, coinId));

        URI fullUri = builder.build().encode().toUri();

        try {
            RestTemplateHelper client = new RestTemplateHelper();

            String result = client.doGet(
                    fullUri.toString(),
                    null,
                    null,
                    String.class);

            JSONArray resultObject = new JSONArray(result);

            ApiResponder resultBuilder = new ApiResponder();

            JSONObject firstObject = resultObject.getJSONObject(0);
            DynamicResponse dynamicResponse = objectMapper.readValue(firstObject.toString(), DynamicResponse.class);
            resultBuilder.setContent(dynamicResponse);

            credentialService.consume(providerName);

            return ResponseBuilder.success(resultBuilder);
        }catch (ServiceException | JsonProcessingException ex) {
            throw new ServiceException(ApplicationCode.ERSP01.getCode(), ApplicationCode.ERSP01.getMessage());
        }
    }

    private String getSiteNameFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();

            String[] parts = host.split("\\.");
            int len = parts.length;
            if (len >= 2) {
                host = parts[len - 2] + "." + parts[len - 1];
            }

            // Remove common TLDs
            host = host.replaceAll("\\.(com|net|org|io|co|gov|edu|info|us|biz)$", "");

            if (host.length() > 0) {
                host = host.substring(0, 1).toUpperCase() + host.substring(1);
            }

            return host;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
}

}
