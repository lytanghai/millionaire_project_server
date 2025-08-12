package com.millionaire_project.millionaire_project.service.service_provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.millionaire_project.millionaire_project.annotation.ServiceProvider;
import com.millionaire_project.millionaire_project.controller.service_provider.ServiceAPIProvider;
import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.stereotype.Service;

@Service
@ServiceProvider(partnerCode = "0002", partnerName = "mobile_finance")
public class MobileFinanceService implements ServiceAPIProvider {

    @Override
    public String getPartnerName() {
        return "mobile_finance";
    }

    @Override
    public ResponseBuilder<ApiResponder> trigger(ApiRequester apiRequester) throws JsonProcessingException {
        System.out.println("MOPB");
        return null;
    }
}
