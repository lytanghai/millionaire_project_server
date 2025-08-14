package com.millionaire_project.millionaire_project.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.millionaire_project.millionaire_project.dto.res.CoinPairSymbol;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

public class CommonUtil {

    public static String getSymbolById(String symbol, int index) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream is = new ClassPathResource("static/pair-symbol.json").getInputStream();
        List<CoinPairSymbol> coins = objectMapper.readValue(is, new TypeReference<>() {});

        return coins.stream()
                .map(CoinPairSymbol::getId)
                .map(id -> {
                    String[] parts = id.split("-");
                    return parts[index];
                })
                .findFirst()
                .orElse(null);
    }
}
