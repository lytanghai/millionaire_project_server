package com.millionaire_project.millionaire_project.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.dto.res.CoinPairSymbol;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    private final static String SUFFIX = "_pair-symbol.json";
    private final static String STATIC = "static/";

    public static String getSymbolById(String symbol, String providerName) throws Exception {
        String fileName = "";
        if(providerName.equals("coin_paprika")) {
            fileName = providerName + SUFFIX;
        }
        fileName = STATIC + fileName;

        ObjectMapper objectMapper = new ObjectMapper();
        InputStream is = new ClassPathResource(fileName).getInputStream();
        List<CoinPairSymbol> coins = objectMapper.readValue(is, new TypeReference<>() {});

        return coins.stream()
                .filter(c -> c.getSymbol().equalsIgnoreCase(symbol))  // find coin by symbol
                .map(CoinPairSymbol::getId)                          // get its id (e.g. "btc-bitcoin")
                .findFirst()
                .orElse(null);
    }

    public static String getCoinUUID(String symbol) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream is = new ClassPathResource(STATIC + "coin_ranking" + SUFFIX).getInputStream();
        Map<String, Object> list = objectMapper.readValue(is, new TypeReference<>() {});

        return list.get(symbol.toUpperCase()).toString();
    }


}
