package com.millionaire_project.millionaire_project.enumz;

public enum RefreshType {
    DAY("DAY"),
    MONTH("MONTH"),
    YEAR("YEAR");

    private String type;

    RefreshType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
