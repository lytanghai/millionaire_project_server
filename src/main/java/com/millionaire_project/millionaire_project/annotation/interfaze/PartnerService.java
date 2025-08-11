package com.millionaire_project.millionaire_project.annotation.interfaze;

import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;

public interface PartnerService {
    default ResponseBuilder<ApiResponder> triggerApi(ApiRequester apiRequester) {
        return null;
    }
}