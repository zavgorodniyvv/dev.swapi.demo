package com.example.swapidemo.service;

import com.example.swapidemo.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getFilms(List<String> urls);
}
