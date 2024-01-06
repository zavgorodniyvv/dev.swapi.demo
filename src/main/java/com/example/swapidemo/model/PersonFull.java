package com.example.swapidemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class PersonFull {
    @JsonProperty("name")
    private String name;
    @JsonProperty("height")
    private String height;
    @JsonProperty("mass")
    private String mass;
    @JsonProperty("hair_color")
    private String hairColor;
    @JsonProperty("skin_color")
    private String skinColor;
    @JsonProperty("eye_color")
    private String eyeColor;
    @JsonProperty("birth_year")
    private String birthYear;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("homeworld")
    private String homeWorld;
    @JsonProperty("films")
    private List<Film> films;
    @JsonProperty("species")
    private List<Species> species;
    @JsonProperty("vehicles")
    private List<Vehicle> vehicles;
    @JsonProperty("starships")
    private List<Starship> starships;
    @JsonProperty("created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private LocalDateTime created;
    @JsonProperty("edited")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private LocalDateTime edited;
    @JsonProperty("url")
    private String url;

    public PersonFull(Person person, List<Film> films, List<Species> species,List<Vehicle> vehicles, List<Starship> starships) {
        this.name = person.getName();
        this.height = person.getHeight();
        this.mass = person.getMass();
        this.hairColor = person.getHairColor();
        this.skinColor = person.getSkinColor();
        this.eyeColor = person.getEyeColor();
        this.birthYear = person.getBirthYear();
        this.gender = person.getGender();
        this.homeWorld = person.getHomeWorld();
        this.films = films;
        this.species = species;
        this.vehicles = vehicles;
        this.starships = starships;
        this.created = person.getCreated();
        this.edited = person.getEdited();
        this.url = person.getUrl();
    }

}
