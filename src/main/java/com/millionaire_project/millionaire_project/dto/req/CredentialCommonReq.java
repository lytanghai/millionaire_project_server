package com.millionaire_project.millionaire_project.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CredentialCommonReq {
    private Integer id;

    @JsonProperty("modify_credential_req")
    private ModifyCredentialReq modifyCredentialReq;


    public CredentialCommonReq(Integer id, ModifyCredentialReq modifyCredentialReq) {
        this.id = id;
        this.modifyCredentialReq = modifyCredentialReq;
    }

    public static class ModifyCredentialReq {

        public ModifyCredentialReq() {}

        @JsonProperty("email")
        private String email;

        @JsonProperty("provider_name")
        private String providerName;

        @JsonProperty("api_key")
        private String apiKey;

        private Integer capped;

        @JsonProperty("refresh_type")
        private String refreshType;

        @JsonProperty("next_refresh_date")
        private Date nextRefreshDate;

        private Boolean active;

        public ModifyCredentialReq(String email, String providerName, String apiKey, Integer capped, String refreshType, Date nextRefreshDate, Boolean active) {
            this.email = email;
            this.providerName = providerName;
            this.apiKey = apiKey;
            this.capped = capped;
            this.refreshType = refreshType;
            this.nextRefreshDate = nextRefreshDate;
            this.active = active;
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
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ModifyCredentialReq getModifyCredentialReq() {
        return modifyCredentialReq;
    }

    public void setModifyCredentialReq(ModifyCredentialReq modifyCredentialReq) {
        this.modifyCredentialReq = modifyCredentialReq;
    }
}
