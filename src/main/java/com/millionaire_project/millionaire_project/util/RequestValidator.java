package com.millionaire_project.millionaire_project.util;

import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.dto.req.RegisterCredentialReq;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import org.springframework.util.StringUtils;

public class RequestValidator {

    public static void validateRequest(RegisterCredentialReq req) {

        if (req == null) {
            throw new ServiceException(ApplicationCode.E000.getCode(), "Request must not be null");
        }
        if (!StringUtils.hasText(req.getEmail())) {
            throw new ServiceException(ApplicationCode.E001.getCode(), ApplicationCode.E001.getMessage());
        }
        if (!StringUtils.hasText(req.getApiKey())) {
            throw new ServiceException(ApplicationCode.E002.getCode(), ApplicationCode.E002.getMessage());
        }

        if(req.getCapped() == null || req.getCapped() <= 0) {
            throw new ServiceException(ApplicationCode.E003.getCode(), ApplicationCode.E003.getMessage());
        }
        // Optionally validate other fields as needed
    }
}
