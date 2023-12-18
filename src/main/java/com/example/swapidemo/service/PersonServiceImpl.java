package com.example.swapidemo.service;

import com.example.swapidemo.exception.SwapiAppException;
import com.example.swapidemo.model.PeopleSearchResult;
import com.example.swapidemo.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService{
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final HttpService httpService;
    private final URLCreator urlCreator;
    private final ObjectMapper swapiObjectMapper;

    public PersonServiceImpl(HttpService httpService, URLCreator urlCreator, ObjectMapper swapiObjectMapper) {
        this.httpService = httpService;
        this.urlCreator = urlCreator;
        this.swapiObjectMapper = swapiObjectMapper;
    }

    @Override
    public Person getPerson(String id) {
        logger.info("Get request for people with id: {}", id);
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

    @Override
    public List<Person> findPersonByName(String name) {
        logger.info("Get request find people by name: {}", name);
        List<Person> people = new ArrayList<>();
        var url = urlCreator.createFindPersonByNameURL(name);
        boolean hasNext= true;
        do {
            String peopleAsString = httpService.sendSingleGetRequest(url);
            PeopleSearchResult peopleSearchResult = null;
            try {
                peopleSearchResult = swapiObjectMapper.readValue(peopleAsString, PeopleSearchResult.class);
            } catch (JsonProcessingException e) {
                logger.error("Could not parse Parse response from server. Reason: {}", e.getMessage());
            }
            if(peopleSearchResult != null && peopleSearchResult.getNext() != null){
                url = peopleSearchResult.getNext();
            } else {
                hasNext = false;
            }
            people.addAll(peopleSearchResult == null ? List.of() : peopleSearchResult.getResults());
        } while(hasNext);

        logger.info("Got response from SWAPI, {} number", people.size());
        return people;
    }
}
