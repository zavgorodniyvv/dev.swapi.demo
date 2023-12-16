package com.example.swapidemo.service;

import com.example.swapidemo.exception.SwapiAppException;
import com.example.swapidemo.model.Film;
import com.example.swapidemo.model.Person;
import com.example.swapidemo.model.PersonFull;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService{
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final HttpService httpService;
    private final URLCreator urlCreator;
    private final ObjectMapper swapiObjectMapper;
    private final FilmService filmService;

    public PersonServiceImpl(HttpService httpService, URLCreator urlCreator, ObjectMapper swapiObjectMapper, FilmService filmService) {
        this.httpService = httpService;
        this.urlCreator = urlCreator;
        this.swapiObjectMapper = swapiObjectMapper;
        this.filmService = filmService;
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
    public PersonFull getPersonWithFullInfo(String id) {
        logger.info("Get request for get full people info with id: {}", id);
        Person person = getPerson(id);
        List<Film> films = getFilmsByPerson(id);
        //TODO implement getting info about species, starships, vehicles

        PersonFull personFull = new PersonFull(person, films, null, null, null);

        logger.info("return Person with full info: {}", personFull);
        return personFull;
    }

    private List<Film> getFilmsByPerson(String id) {
        List<String> urls = getPerson(id).getFilms();
        return filmService.getFilms(urls);
    }
}
