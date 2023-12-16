package com.example.swapidemo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("swapi")
public class SwapiProperties {
    private String baseUrl;
    private String suffixPeople;

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
}
