package com.millionaire_project.millionaire_project.constant;

public enum TopicOperation {

    GET_COIN_DETAIL(Static.GET_COIN_DETAIL,"coins/{coin_id}","Returns detailed descriptive information about a single coin, without price or volume data"),
    GET_TODAY_OHLC(Static.GET_TODAY_OHLC, "coins/{coin_id}/ohlcv/today","Returns Open/High/Low/Close values with volume and market capitalization for the current day. Data can change every each request until actual close of the day at 23:59:59"),
    GET_EXCHANGE_PLATFORM(Static.GET_EXCHANGE_PLATFORM, "coins/{coin_id}/markets",""),
    CMC_MARKET_PAIR_LATEST(Static.CMC_MARKET_PAIR_LATEST, "/market-pairs/latest?symbol={symbol}&convert=USD", ""),
    CG_GET_COIN_DETAIL(Static.CG_GET_COIN_DETAIL, "coins/{coin_id}", ""),
    CR_GET_COIN_DETAIL(Static.CR_GET_COIN_DETAIL, "coin/{coin_id}", ""),
    ;


    private final String topicName;
    private final String endpoint;
    private final String description;

    TopicOperation(String topicName,  String endpoint, String description) {
        this.topicName = topicName;
        this.endpoint = endpoint;
        this.description = description;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getDescription() {
        return description;
    }

    public String getEndpoint() {
        return endpoint;
    }

    // Helper to find topic by name
    public static TopicOperation fromTopicName(String topicName) {
        for (TopicOperation t : values()) {
            if (t.topicName.equalsIgnoreCase(topicName)) {
                return t;
            }
        }
        return null; // or throw exception
    }
}
