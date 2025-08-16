package com.millionaire_project.millionaire_project.service;

import com.millionaire_project.millionaire_project.constant.ApplicationCode;
import com.millionaire_project.millionaire_project.constant.Static;
import com.millionaire_project.millionaire_project.dto.req.ConsumeReq;
import com.millionaire_project.millionaire_project.dto.req.CredentialCommonReq;
import com.millionaire_project.millionaire_project.dto.req.FilterPropertyReq;
import com.millionaire_project.millionaire_project.dto.req.RegisterCredentialReq;
import com.millionaire_project.millionaire_project.dto.res.CredentialResp;
import com.millionaire_project.millionaire_project.entity.CredentialEntity;
import com.millionaire_project.millionaire_project.exception.ServiceException;
import com.millionaire_project.millionaire_project.repository.CredentialRepository;
import com.millionaire_project.millionaire_project.util.DateUtil;
import com.millionaire_project.millionaire_project.util.RequestValidator;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    @Autowired private CredentialRepository credentialRepository;

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
        if (credential != null) {
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
        throw new ServiceException(ApplicationCode.E005.getCode(), ApplicationCode.E005.getMessage());
    }

    public CredentialEntity findCredentialRecord(int id) {
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

    public void consume(String providerName) {
            ConsumeReq request = new ConsumeReq();
            request.setProviderName(providerName);
            this.consume(request);
    }

    public ResponseBuilder<CredentialEntity> consume(ConsumeReq req) {
        if(req.getProviderName().isEmpty())
            throw new ServiceException(ApplicationCode.E007.getCode(), ApplicationCode.E007.getMessage());

        List<CredentialEntity> credentials = credentialRepository.findByProviderName(req.getProviderName());

        if(credentials.isEmpty()) {
            throw new ServiceException(ApplicationCode.W001.getCode(), ApplicationCode.W001.getMessage());
        } else {
            if(DateUtil.isTodayFirstDayOfMonth()) {
                List<CredentialEntity> toUpdate = credentials.stream()
                        .filter(c -> DateUtil.refreshBalance(c.getNextRefreshDate()))
                        .peek(c -> {
                            c.setRemaining(c.getCapped());
                            c.setNextRefreshDate(Static.DAY.equals(c.getRefreshType())
                                    ? DateUtil.getNextDateAtMidnight()
                                    : DateUtil.getFirstDateOfNextMonthUtilDate());
                        })
                        .collect(Collectors.toList());
                credentialRepository.saveAll(toUpdate);
            }

            Optional<CredentialEntity> getRemaining = credentials.stream()
                    .filter(c -> c.getProviderName().equalsIgnoreCase(req.getProviderName()))
                    .max(Comparator.comparingInt(CredentialEntity::getRemaining));

            CredentialEntity entity = getRemaining.get();

            entity.setRemaining(entity.getRemaining() -1);
            if(entity.getRemaining() - 1 == 0)
                entity.setActive(false);

            credentialRepository.save(entity);

            return ResponseBuilder.success(entity);
        }
    }

    public void updateRemaining(CredentialEntity entity) {
        entity.setRemaining(entity.getRemaining() -1);
        if(entity.getRemaining() - 1 == 0)
            entity.setActive(false);

        credentialRepository.save(entity);
    }

    public CredentialEntity findTheMostUsages(String providerName) {
        List<CredentialEntity> credentials = credentialRepository.findByProviderName(providerName);

        if (credentials.isEmpty()) {
            throw new ServiceException(ApplicationCode.W001.getCode(), ApplicationCode.W001.getMessage());
        } else {
            if (DateUtil.isTodayFirstDayOfMonth()) {
                List<CredentialEntity> toUpdate = credentials.stream()
                        .filter(c -> DateUtil.refreshBalance(c.getNextRefreshDate()))
                        .peek(c -> {
                            c.setRemaining(c.getCapped());
                            c.setNextRefreshDate(Static.DAY.equals(c.getRefreshType())
                                    ? DateUtil.getNextDateAtMidnight()
                                    : DateUtil.getFirstDateOfNextMonthUtilDate());
                        })
                        .collect(Collectors.toList());
                credentialRepository.saveAll(toUpdate);
            }

            Optional<CredentialEntity> getRemaining = credentials.stream()
                    .filter(c -> c.getProviderName().equalsIgnoreCase(providerName))
                    .max(Comparator.comparingInt(CredentialEntity::getRemaining));

            return getRemaining.get();
        }
    }
}
