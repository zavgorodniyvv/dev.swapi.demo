package com.example.swapidemo.service;

import com.example.swapidemo.exception.SwapiAppException;
import com.example.swapidemo.model.Film;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService{
    private static final Logger logger = LoggerFactory.getLogger(FilmServiceImpl.class);

    private final HttpService httpService;
    private final ObjectMapper swapiObjectMapper;

    public FilmServiceImpl(HttpService httpService, ObjectMapper swapiObjectMapper) {
        this.httpService = httpService;
        this.swapiObjectMapper = swapiObjectMapper;
    }

    @Override
    public List<Film> getFilms(List<String> urls) {
        logger.info("Get request for get films by urls: {}", urls);
        List<String> filmsAsString = httpService.sendMultipleGetRequest(urls);

        return filmsAsString.stream()
                .map(this::convertToFilm)
                .toList();
    }

    private Film convertToFilm(String filmAsString) {
        Film film;
        try {
            film = swapiObjectMapper.readValue(filmAsString, Film.class);
        } catch (JsonProcessingException e) {
            logger.error("Could not parse response from server. Reason: {}", e.getMessage());
            throw new SwapiAppException(e.getMessage());
        }
        logger.info("Got response from SWAPI: {}", film);
        return film;
    }
}
