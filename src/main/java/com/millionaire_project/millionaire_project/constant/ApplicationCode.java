package com.millionaire_project.millionaire_project.constant;

public enum ApplicationCode {

    E000("E000","Request body must not be null!"),
    E001("E001","Email must not be null or empty!"),
    E002("E002","API KEY must not be null or empty"),
    E003("E003","Capped must not be null or less than 0"),
    E500("E005", "Record does not exist!");

    private String code;
    private String message;

    ApplicationCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
