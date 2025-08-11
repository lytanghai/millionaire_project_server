package com.millionaire_project.millionaire_project.annotation;

import org.json.JSONObject;

public class RequestDTO {
    private int partnerCode;
    private String partnerName;
    private JSONObject requestData;

    public int getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(int partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public JSONObject getRequestData() {
        return requestData;
    }

    public void setRequestData(JSONObject requestData) {
        this.requestData = requestData;
    }
}
