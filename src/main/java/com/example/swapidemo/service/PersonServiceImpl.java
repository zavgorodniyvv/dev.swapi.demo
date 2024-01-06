package com.example.swapidemo.service;

import com.example.swapidemo.exception.SwapiAppException;
import com.example.swapidemo.model.Film;
import com.example.swapidemo.model.People;
import com.example.swapidemo.model.PeopleSearchResult;
import com.example.swapidemo.model.Person;
import com.example.swapidemo.model.PersonFull;
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
    private final FilmService filmService;
    private final CacheService<String, Person> cacheService;

    public PersonServiceImpl(HttpService httpService, URLCreator urlCreator, ObjectMapper swapiObjectMapper,
                             FilmService filmService, CacheService<String, Person> cacheService) {
        this.httpService = httpService;
        this.urlCreator = urlCreator;
        this.swapiObjectMapper = swapiObjectMapper;
        this.filmService = filmService;
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
        logger.info("Got response for people from SWAPI: {}", people.size());
        return people;
    }
    private Person getPersonFromCache(String id) {
        return cacheService.get(id);
    }
}
