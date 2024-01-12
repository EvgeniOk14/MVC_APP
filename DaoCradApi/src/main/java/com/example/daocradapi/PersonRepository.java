package com.example.daocradapi;

import com.example.daocradapi.models.Person;

import java.util.List;

public interface PersonRepository
{
    Person findPersonByEmail(String email);

    public List<Person> findBySurname(String surname);

}
