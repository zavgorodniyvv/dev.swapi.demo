package com.example.swapidemo.service;

import com.example.swapidemo.exception.SwapiAppException;
import com.example.swapidemo.model.People;
import com.example.swapidemo.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
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
    public List<Person> getAllPeople() {
        List<Person> people = new ArrayList<>();
        var url = urlCreator.createPeopleURL();
        boolean getAll = true;
        People allPeople = new People();
        do {
            String peopleAsString = httpService.sendSingleGetRequest(url);
            try {
                allPeople = swapiObjectMapper.readValue(peopleAsString, People.class);
            } catch (JsonProcessingException e) {
                logger.error("Could not parse Parse response from server. Reason: {}", e.getMessage());
            }
            people.addAll(allPeople.getResults());
            if (allPeople.getNext() != null) {
                url = allPeople.getNext();
            } else {
                getAll = false;
            }
        } while (getAll);
        logger.info("Got response for people from SWAPI: {}");
        return people;
    }
}
