package com.millionaire_project.millionaire_project.service.service_provider;

import com.millionaire_project.millionaire_project.annotation.PartnerServiceRegistry;
import com.millionaire_project.millionaire_project.annotation.ServiceProvider;
import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.entity.CredentialEntity;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.repository.CredentialRepository;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import com.millionaire_project.millionaire_project.util.RestTemplateHelper;
import com.millionaire_project.millionaire_project.util.RestTemplateHttp;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@ServiceProvider(partnerCode = "0001", partnerName = "crypto_panic")
public class CryptoPanicService {

    private final Logger log = LoggerFactory.getLogger(CryptoPanicService.class);
    @Autowired private PartnerServiceRegistry registry;
    @Autowired private CredentialRepository credentialRepository;

    public ResponseBuilder<ApiResponder> triggerApi(ApiRequester dto) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if(dto.getTopicName().isEmpty())
            throw new ServiceException(ApplicationCode.E006.getCode(), ApplicationCode.E006.getMessage());

        String providerName = dto.getProviderName();

        List<CredentialEntity> credentialObject = credentialRepository.findByProviderName(providerName);
        Optional<CredentialEntity> getRemaining = credentialObject.stream()
                .filter(c -> c.getProviderName().equalsIgnoreCase(providerName))
                .max(Comparator.comparingInt(CredentialEntity::getRemaining));

        CredentialEntity entity = getRemaining.get();

        Map<String,Object> payloadReq = dto.getPayload();

        String currency = payloadReq.getOrDefault("currency", "").toString();

        RestTemplateHelper client = new RestTemplateHelper();
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(Static.AUTH_TOKEN, entity.getApiKey());
        queryParams.put(Static.CURRENCIES, currency);

        System.out.println("URL: " + Static.CRYPTO_PANIC_BASE_URL + queryParams);

        String result = client.doGet(Static.CRYPTO_PANIC_BASE_URL, queryParams, null, String.class);

        ApiResponder resultBuilder = new ApiResponder();
        resultBuilder.setContent(new JSONObject(result));

        return ResponseBuilder.success(resultBuilder);
    }
}
