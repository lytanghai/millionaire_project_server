package com.millionaire_project.millionaire_project.controller;

import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.service.service_provider.CryptoPanicService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ProviderController {

    @Autowired private CryptoPanicService service;

    @PostMapping("/trigger")
    public ResponseBuilder<ApiResponder> check(@RequestBody ApiRequester dto) {
        return service.triggerApi(dto);
    }
}
