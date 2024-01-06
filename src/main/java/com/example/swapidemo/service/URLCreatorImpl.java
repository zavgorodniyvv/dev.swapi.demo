package com.example.swapidemo.service;

import com.example.swapidemo.config.SwapiProperties;
import org.springframework.stereotype.Service;

@Service
public class URLCreatorImpl implements URLCreator{

    private final SwapiProperties swapi;

    public URLCreatorImpl(SwapiProperties swapi) {
        this.swapi = swapi;
    }

    @Override
    public String createPersonByIdURL(String id) {
        return swapi.getBaseUrl() + swapi.getSuffixPeople() + id;
    }

    @Override
    public String createPeopleURL() {
        return swapi.getBaseUrl() + swapi.getSuffixPeople();
    }

}
