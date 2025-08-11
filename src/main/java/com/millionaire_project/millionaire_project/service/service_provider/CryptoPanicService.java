package com.millionaire_project.millionaire_project.service.service_provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.annotation.PartnerServiceRegistry;
import com.millionaire_project.millionaire_project.annotation.ServiceProvider;
import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.dto.res.DynamicResponse;
import com.millionaire_project.millionaire_project.entity.CredentialEntity;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.repository.CredentialRepository;
import com.millionaire_project.millionaire_project.service.CredentialService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import com.millionaire_project.millionaire_project.util.RestTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
@ServiceProvider(partnerCode = "0001", partnerName = "crypto_panic")
public class CryptoPanicService {

    private final Logger log = LoggerFactory.getLogger(CryptoPanicService.class);
    @Autowired private PartnerServiceRegistry registry;
    @Autowired private CredentialRepository credentialRepository;
    @Autowired private CredentialService credentialService;

    public static final List<String> AVAILABLE_FILTER = Arrays.asList("rising", "hot", "bullish", "bearish", "important", "saved" , "lol");
    public static final List<String> AVAILABLE_KIND = Arrays.asList("news", "media", "all");

    public ResponseBuilder<ApiResponder> triggerApi(ApiRequester dto) throws JsonProcessingException {
        if (dto.getTopicName() == null || dto.getTopicName().isEmpty())
            throw new ServiceException(ApplicationCode.E006.getCode(), ApplicationCode.E006.getMessage());

        String providerName = dto.getProviderName();

        List<CredentialEntity> credentialObject = credentialRepository.findByProviderName(providerName);
        Optional<CredentialEntity> getRemaining = credentialObject.stream()
                .filter(c -> c.getProviderName().equalsIgnoreCase(providerName))
                .max(Comparator.comparingInt(CredentialEntity::getRemaining));

        CredentialEntity entity = getRemaining.orElseThrow(() ->
                new ServiceException(ApplicationCode.W001.getCode(), ApplicationCode.W001.getMessage()));

        Map<String, Object> payloadReq = dto.getPayload();
        String currency = payloadReq.getOrDefault(Static.CURRENCY, "").toString();
        String kind = payloadReq.getOrDefault(Static.KIND, "").toString();
        String filter = payloadReq.getOrDefault(Static.FILTER, "").toString();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Static.CRYPTO_PANIC_BASE_URL)
                .queryParam(Static.AUTH_TOKEN, entity.getApiKey());

        if (!kind.isEmpty() && AVAILABLE_KIND.contains(kind))
            builder.queryParam(Static.KIND, kind);

        if(!currency.isEmpty())
            builder.queryParam(Static.CURRENCIES, currency);

        if(!filter.isEmpty() && AVAILABLE_FILTER.contains(filter))
                builder.queryParam(Static.FILTER, filter);

        URI fullUri = builder.build().encode().toUri();

        log.info("Calling external API with URL: {}", fullUri);

        RestTemplateHelper client = new RestTemplateHelper();
        String result = client.doGet(fullUri.toString(), null, null, String.class);

        ApiResponder resultBuilder = new ApiResponder();
        ObjectMapper mapper = new ObjectMapper();

        resultBuilder.setContent(mapper.readValue(result, DynamicResponse.class));
        credentialService.consume(providerName);

        return ResponseBuilder.success(resultBuilder);
    }

}
