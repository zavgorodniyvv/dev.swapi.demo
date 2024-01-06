package com.example.swapidemo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("swapi")
public class SwapiProperties {
    private String baseUrl;
    private String suffixPeople;
    private String findSuffix;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSuffixPeople() {
        return suffixPeople;
    }

    public void setSuffixPeople(String suffixPeople) {
        this.suffixPeople = suffixPeople;
    }

    public String getFindSuffix() {
        return findSuffix;
    }

    public void setFindSuffix(String findSuffix) {
        this.findSuffix = findSuffix;
    }
}
