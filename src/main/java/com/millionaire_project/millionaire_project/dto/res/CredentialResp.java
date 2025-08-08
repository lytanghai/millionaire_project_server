package com.millionaire_project.millionaire_project.dto.res;

import com.millionaire_project.millionaire_project.entity.CredentialEntity;

import java.util.Date;

public class CredentialResp {
    private Integer id;
    private String email;
    private String providerName;
    private String apiKey;
    private Integer capped;
    private String refreshType;
    private Date currentDate;
    private Date nextRefreshDate;
    private Boolean active;
    private Date createdAt;

    public CredentialResp(Integer id, String email, String providerName, String apiKey, Integer capped, String refreshType, Date currentDate, Date nextRefreshDate, Boolean active, Date createdAt) {
        this.id = id;
        this.email = email;
        this.providerName = providerName;
        this.apiKey = apiKey;
        this.capped = capped;
        this.refreshType = refreshType;
        this.currentDate = currentDate;
        this.nextRefreshDate = nextRefreshDate;
        this.active = active;
        this.createdAt = createdAt;
    }

    public CredentialResp() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getCapped() {
        return capped;
    }

    public void setCapped(Integer capped) {
        this.capped = capped;
    }

    public String getRefreshType() {
        return refreshType;
    }

    public void setRefreshType(String refreshType) {
        this.refreshType = refreshType;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getNextRefreshDate() {
        return nextRefreshDate;
    }

    public void setNextRefreshDate(Date nextRefreshDate) {
        this.nextRefreshDate = nextRefreshDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static CredentialResp fromEntity(CredentialEntity credentialEntity) {
        if (credentialEntity == null) {
            return null;
        }

        CredentialResp resp = new CredentialResp();
        resp.setId(credentialEntity.getId());
        resp.setEmail(credentialEntity.getEmail());
        resp.setProviderName(credentialEntity.getProviderName());
        resp.setApiKey(credentialEntity.getApiKey());
        resp.setCapped(credentialEntity.getCapped());
        resp.setRefreshType(credentialEntity.getRefreshType());
        resp.setCurrentDate(credentialEntity.getCurrentDate());
        resp.setNextRefreshDate(credentialEntity.getNextRefreshDate());
        resp.setActive(credentialEntity.getActive());
        resp.setCreatedAt(credentialEntity.getCreatedAt());

        return resp;
    }
}
