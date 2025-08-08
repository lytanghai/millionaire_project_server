package com.millionaire_project.millionaire_project.service;

import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.dto.req.CredentialCommonReq;
import com.millionaire_project.millionaire_project.dto.req.FilterPropertyReq;
import com.millionaire_project.millionaire_project.dto.req.RegisterCredentialReq;
import com.millionaire_project.millionaire_project.dto.res.CredentialResp;
import com.millionaire_project.millionaire_project.entity.CredentialEntity;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.repository.CredentialRepository;
import com.millionaire_project.millionaire_project.util.DateUtil;
import com.millionaire_project.millionaire_project.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    public void registerCredential(RegisterCredentialReq req) {
        CredentialEntity credentialEntity = new CredentialEntity();

        RequestValidator.validateRequest(req);

        credentialEntity.setApiKey(req.getApiKey());
        credentialEntity.setEmail(req.getEmail());
        credentialEntity.setCapped(req.getCapped());
        credentialEntity.setRemaining(req.getCapped());
        credentialEntity.setActive(req.getActive());
        credentialEntity.setCreatedAt(DateUtil.convertToPhnomPenhDate(req.getCreatedAt()));
        credentialEntity.setCurrentDate(DateUtil.convertToPhnomPenhDate(req.getCreatedAt()));
        credentialEntity.setProviderName(req.getProviderName());
        credentialEntity.setRefreshType(req.getRefreshType());

        credentialEntity.setNextRefreshDate(req.getRefreshType().equals(Static.DAY)
                ? DateUtil.getNextDateAtMidnight()
                : DateUtil.getFirstDateOfNextMonthUtilDate());

        credentialRepository.save(credentialEntity);
    }

    public void deleteCredential(Integer id) {
        credentialRepository.deleteById(id);
    }

    public void modifyCredential(Integer id, CredentialCommonReq.ModifyCredentialReq req) {
        CredentialEntity credential = findCredentialRecord(id);
        if(credential != null) {
            if (req.getEmail() != null && !req.getEmail().trim().isEmpty()) {
                credential.setEmail(req.getEmail());
            }
            if (req.getProviderName() != null && !req.getProviderName().trim().isEmpty()) {
                credential.setProviderName(req.getProviderName());
            }
            if (req.getApiKey() != null && !req.getApiKey().trim().isEmpty()) {
                credential.setApiKey(req.getApiKey());
            }
            if (req.getCapped() != null) {
                credential.setCapped(req.getCapped());
            }

            if (req.getActive() != null) {
                credential.setActive(req.getActive());
            }

            if (req.getRefreshType() != null && !req.getRefreshType().trim().isEmpty()) {
                credential.setRefreshType(req.getRefreshType());
            }

            if (req.getNextRefreshDate() != null) {
                credential.setNextRefreshDate(DateUtil.convertToPhnomPenhDate(req.getNextRefreshDate()));
            }

            credentialRepository.save(credential);
            return;
        }
        throw new ServiceException(ApplicationCode.E500.getCode(),ApplicationCode.E500.getMessage());
    }

    private CredentialEntity findCredentialRecord(int id) {
        return credentialRepository.findById(id).orElse(null);
    }

    public Page<CredentialResp> list(FilterPropertyReq filterPropertyReq) {
        Pageable pageable = PageRequest.of(
                filterPropertyReq.getPage(),
                filterPropertyReq.getSize(),
                Sort.by("id").descending()
        );

        Page<CredentialEntity> page = credentialRepository.findByFilterProperties(
                filterPropertyReq.getId(),
                filterPropertyReq.getEmail(),
                filterPropertyReq.getProviderName(),
                filterPropertyReq.getRefreshType(),
                filterPropertyReq.isActive(),
                pageable
        );

        return page.map(CredentialResp::fromEntity);
    }

}
