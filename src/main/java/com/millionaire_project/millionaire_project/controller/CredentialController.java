package com.millionaire_project.millionaire_project.controller;

import com.millionaire_project.millionaire_project.dto.req.CredentialCommonReq;
import com.millionaire_project.millionaire_project.dto.req.FilterPropertyReq;
import com.millionaire_project.millionaire_project.dto.req.RegisterCredentialReq;
import com.millionaire_project.millionaire_project.dto.res.CredentialResp;
import com.millionaire_project.millionaire_project.service.CredentialService;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credential")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @PostMapping("/register")
    public void registerCredential(@RequestBody RegisterCredentialReq req) {
        credentialService.registerCredential(req);
    }

    @PostMapping("/delete/{id}")
    public void deleteCredential(@PathVariable("id")CredentialCommonReq req) {
        credentialService.deleteCredential(req.getId());
    }

    @PatchMapping("/modify/{id}")
    public void modifyCredential(@PathVariable("id")Integer id, @RequestBody CredentialCommonReq.ModifyCredentialReq req) {
        credentialService.modifyCredential(id,req);
    }

    @GetMapping("/fetch")
    public ResponseBuilder<Page<CredentialResp>> list(@RequestBody FilterPropertyReq filterPropertyReq) {
        return ResponseBuilder.success(credentialService.list(filterPropertyReq));
    }
}
