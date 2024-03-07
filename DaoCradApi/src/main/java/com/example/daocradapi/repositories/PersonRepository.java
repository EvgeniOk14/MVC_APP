package com.example.daocradapi.repositories;

import com.example.daocradapi.models.person.Person;

import java.util.List;

public interface PersonRepository
{
    Person findPersonByEmail(String email);

    public List<Person> findBySurname(String surname);

}
