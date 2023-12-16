package com.example.swapidemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Species {
    @JsonProperty("name")
    private String name;
    @JsonProperty("classification")
    private String classification;
    @JsonProperty("designation")
    private String designation;
    @JsonProperty("average_height")
    private String averageHeight;
    @JsonProperty("skin_colors")
    private String skinColors;
    @JsonProperty("hair_colors")
    private String hairColors;
    @JsonProperty("eye_colors")
    private String eyeColors;
    @JsonProperty("average_lifespan")
    private String averageLifespan;
    @JsonProperty("homeworld")
    private List<String> homeWorld;
    @JsonProperty("language")
    private String language;
    @JsonProperty("people")
    private List<String> people;
    @JsonProperty("films")
    private List<String> films;
    @JsonProperty("created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private LocalDateTime created;
    @JsonProperty("edited")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private LocalDateTime edited;
    @JsonProperty("url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAverageHeight() {
        return averageHeight;
    }

    public void setAverageHeight(String averageHeight) {
        this.averageHeight = averageHeight;
    }

    public String getSkinColors() {
        return skinColors;
    }

    public void setSkinColors(String skinColors) {
        this.skinColors = skinColors;
    }

    public String getHairColors() {
        return hairColors;
    }

    public void setHairColors(String hairColors) {
        this.hairColors = hairColors;
    }

    public String getEyeColors() {
        return eyeColors;
    }

    public void setEyeColors(String eyeColors) {
        this.eyeColors = eyeColors;
    }

    public String getAverageLifespan() {
        return averageLifespan;
    }

    public void setAverageLifespan(String averageLifespan) {
        this.averageLifespan = averageLifespan;
    }

    public List<String> getHomeWorld() {
        return homeWorld;
    }

    public void setHomeWorld(List<String> homeWorld) {
        this.homeWorld = homeWorld;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getEdited() {
        return edited;
    }

    public void setEdited(LocalDateTime edited) {
        this.edited = edited;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Species species = (Species) o;
        return Objects.equals(name, species.name) && Objects.equals(classification, species.classification) && Objects.equals(designation, species.designation) && Objects.equals(averageHeight, species.averageHeight) && Objects.equals(skinColors, species.skinColors) && Objects.equals(hairColors, species.hairColors) && Objects.equals(eyeColors, species.eyeColors) && Objects.equals(averageLifespan, species.averageLifespan) && Objects.equals(homeWorld, species.homeWorld) && Objects.equals(language, species.language) && Objects.equals(people, species.people) && Objects.equals(films, species.films) && Objects.equals(created, species.created) && Objects.equals(edited, species.edited) && Objects.equals(url, species.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, classification, designation, averageHeight, skinColors, hairColors, eyeColors, averageLifespan, homeWorld, language, people, films, created, edited, url);
    }

    @Override
    public String toString() {
        return "Species{" +
                "name='" + name + '\'' +
                ", classification='" + classification + '\'' +
                ", designation='" + designation + '\'' +
                ", averageHeight='" + averageHeight + '\'' +
                ", skinColors='" + skinColors + '\'' +
                ", hairColors='" + hairColors + '\'' +
                ", eyeColors='" + eyeColors + '\'' +
                ", averageLifespan='" + averageLifespan + '\'' +
                ", homeWorld=" + homeWorld +
                ", language='" + language + '\'' +
                ", people=" + people +
                ", films=" + films +
                ", created=" + created +
                ", edited=" + edited +
                ", url='" + url + '\'' +
                '}';
    }
}
