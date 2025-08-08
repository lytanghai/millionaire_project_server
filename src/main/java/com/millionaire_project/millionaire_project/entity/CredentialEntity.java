package com.millionaire_project.millionaire_project.entity;

import com.millionaire_project.millionaire_project.constant.Static;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "credential")
public class CredentialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Static.CREDENTIAL_SEQ)
    @SequenceGenerator(name = Static.CREDENTIAL_SEQ , sequenceName = Static.CREDENTIAL_SEQ, allocationSize = 1)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "provider_name")
    private String providerName;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "capped")
    private Integer capped;

    @Column(name = "remaining")
    private Integer remaining;

    @Column(name = "refresh_type")
    private String refreshType;

    @Column(name = "\"current_date\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date currentDate;

    @Column(name = "next_refresh_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextRefreshDate;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

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

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
