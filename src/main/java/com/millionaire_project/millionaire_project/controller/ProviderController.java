package com.millionaire_project.millionaire_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.dto.req.ApiRequester;
import com.millionaire_project.millionaire_project.dto.res.ApiResponder;
import com.millionaire_project.millionaire_project.service.service_provider.CryptoPanicService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/service")
@Tag(name = "Service Provider -> Free API", description = "Operations related to API Site")
public class ProviderController {

    @Autowired private CryptoPanicService service;

    @PostMapping("/trigger")
    @Operation(
            summary = "Trigger API Requests To Free API Sites",
            description = "Crypto Panic: "
    )
    public ResponseBuilder<ApiResponder> check(@RequestBody ApiRequester dto) throws JsonProcessingException {
        return service.triggerApi(dto);
    }

    @GetMapping("/help")
    public Map<String, Object> instruction() {
        JSONObject instruction = new JSONObject();
        JSONObject cryptoPanic = new JSONObject();
        JSONObject cryptoPanicUri = new JSONObject();
        cryptoPanic.put("base_url", Static.CRYPTO_PANIC_BASE_URL);
        cryptoPanicUri.put("&currencies", "Eg. BTC,BNB,ENA");
        cryptoPanicUri.put("&filter", CryptoPanicService.AVAILABLE_FILTER.toString());
        cryptoPanicUri.put("&kind", CryptoPanicService.AVAILABLE_KIND.toString());

        cryptoPanic.put("available_queries", cryptoPanicUri);

        instruction.put("crypto_panic", cryptoPanic);
        return instruction.toMap();
    }
}
