package com.millionaire_project.millionaire_project.dto.res;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

public class ApiResponder {
    private String url;
    private String topic;
    private JSONObject content;
    private HttpStatus status;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
