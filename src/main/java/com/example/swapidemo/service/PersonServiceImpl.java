package com.example.swapidemo.service;

import com.example.swapidemo.exception.SwapiAppException;
import com.example.swapidemo.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService{
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final HttpService httpService;
    private final URLCreator urlCreator;
    private final ObjectMapper swapiObjectMapper;
    private final CacheService<String, Person> cacheService;

    public PersonServiceImpl(HttpService httpService, URLCreator urlCreator, ObjectMapper swapiObjectMapper, CacheService<String, Person> cacheService) {
        this.httpService = httpService;
        this.urlCreator = urlCreator;
        this.swapiObjectMapper = swapiObjectMapper;
        this.cacheService = cacheService;
    }


    @Override
    public Person getPerson(String id) {
        logger.info("Get request for people with id: {}", id);
        Person person = getPersonFromCache(id);
        if (person == null) {
            logger.info("Person not found in cache. Will get it from SWAPI");
            person = getPersonFromSwapi(id);
            cacheService.put(id, person);
        }
        logger.info("Returning person from cache: {}", person);
        return person;
    }

    private Person getPersonFromSwapi(String id) {
        var url = urlCreator.createPersonByIdURL(id);
        String peopleAsString = httpService.sendSingleGetRequest(url);

        Person person;
        try {
            person = swapiObjectMapper.readValue(peopleAsString, Person.class);
        } catch (JsonProcessingException e) {
            logger.error("Could not parse Parse response from server. Reason: {}", e.getMessage());
            throw new SwapiAppException(e.getMessage());
        }
        logger.info("Got response from SWAPI: {}", person);
        return person;
    }

    private Person getPersonFromCache(String id) {
        return cacheService.get(id);
    }
}
