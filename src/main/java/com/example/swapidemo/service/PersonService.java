package com.example.swapidemo.service;

import com.example.swapidemo.model.Person;
import com.example.swapidemo.model.PersonFull;

public interface PersonService {
    Person getPerson(String id);

    PersonFull getPersonWithFullInfo(String id);
}
