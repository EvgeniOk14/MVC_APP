package com.example.daocrud.repository;

import com.example.daocrud.models.Person;

import java.util.List;

public interface PersonRepository
{
    Person findPersonByEmail(String email);

    public List<Person> findBySurname(String surname);

}
