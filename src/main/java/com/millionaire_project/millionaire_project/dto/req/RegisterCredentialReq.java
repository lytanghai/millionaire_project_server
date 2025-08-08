package com.millionaire_project.millionaire_project.dto.req;

import com.sun.istack.NotNull;

import java.util.Date;

public class RegisterCredentialReq {

    @NotNull
    private String email;

    @NotNull
    private String providerName;

    @NotNull
    private String apiKey;

    @NotNull
    private Integer capped;

    @NotNull
    private String refreshType;

    @NotNull
    private Date currentDate = new Date();

    private Date nextRefreshDate;

    private Boolean active = true;

    private Date createdAt = new Date();

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
}
