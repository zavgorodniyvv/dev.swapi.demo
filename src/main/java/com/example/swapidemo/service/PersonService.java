package com.example.swapidemo.service;

import com.example.swapidemo.model.Person;

import java.util.List;

public interface PersonService {
    Person getPerson(String id);

    List<Person> getAllPeople();

    PersonFull getPersonWithFullInfo(String id);

    List<Person> findPersonByName(String name);
}
