package com.millionaire_project.millionaire_project.controller.service_provider;

import com.millionaire_project.millionaire_project.annotation.RequestDTO;
import com.millionaire_project.millionaire_project.annotation.ServiceProvider;
import com.millionaire_project.millionaire_project.annotation.interfaze.PartnerService;
import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;

@ServiceProvider(partnerName = "Crypto Panic", partnerCode = "0001")
public class CryptoPanicController implements PartnerService {

    @Override
    public ResponseBuilder<ApiResponder> triggerApi(ApiRequester apiRequester) {
        System.out.println("I34234234234234N");
        return null;
    }

    //Fetch News Based on Currency

}
