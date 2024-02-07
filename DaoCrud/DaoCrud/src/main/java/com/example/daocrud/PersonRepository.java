package com.example.daocrud;

import com.example.daocrud.models.Person;

import java.util.List;

public interface PersonRepository
{
    Person findPersonByEmail(String email);

    public List<Person> findBySurname(String surname);

}
