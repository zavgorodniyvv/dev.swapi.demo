package com.example.swapidemo.controller;
import com.example.swapidemo.model.Person;
import com.example.swapidemo.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PeopleController {

    private final PersonService personService;

    public PeopleController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/people/{id}")
    public ResponseEntity<Person> getPeople(@PathVariable String id) {
        return new ResponseEntity<>(personService.getPerson(id), HttpStatus.OK);
    }

    @GetMapping("/people")
    public ResponseEntity<List<Person>> findPersonByName(@RequestParam String name) {
        return new ResponseEntity<>(personService.findPersonByName(name), HttpStatus.OK);
    }

    @GetMapping("/people")
    public ResponseEntity<List<Person>> getAllPeople() {
        return new ResponseEntity<>(personService.getAllPeople(), HttpStatus.OK);
    }

    @GetMapping("/people/full/{id}")
    public ResponseEntity<PersonFull> getPeopleFull(@PathVariable String id) {
        return new ResponseEntity<>(personService.getPersonWithFullInfo(id), HttpStatus.OK);
    }
}
