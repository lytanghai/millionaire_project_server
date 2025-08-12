package com.millionaire_project.millionaire_project.controller.service_provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;

public interface ServiceAPIProvider {
    String getPartnerName();

    ResponseBuilder<ApiResponder> trigger(ApiRequester apiRequester) throws JsonProcessingException;
}
