package com.example.daocrud.dao;

import com.example.daocrud.models.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;

    private List<Person> people;
    {
        people = new ArrayList<>();
        people.add(new Person(++PEOPLE_COUNT, "Tom", "Tomson", 34, "mail@Tom.com"));
        people.add(new Person(++PEOPLE_COUNT, "Bob", "Bobson", 24, "mail@Bob.com"));
        people.add(new Person(++PEOPLE_COUNT, "Mike", "Mikson", 20, "mail@Mike.com"));
        people.add(new Person(++PEOPLE_COUNT, "Katy", "Katson", 38, "mail@Katy.com"));
        people.add(new Person(++PEOPLE_COUNT, "Ann", "Annson", 42, "mail@Ann.com"));
    }

    public List<Person> index()
    {
        return people;
    }

    /** метод выводит всех пользователей  **/
    public Person show(int id)
    {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    /** метод сохранят всех пользователей **/
    public void save(Person person)
    {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    /** метод обновляет всех пользователей **/
    public void update(int id, Person updatedPerson)
    {
            Person personToBeUdated = show(id);
            personToBeUdated.setName(updatedPerson.getName());
            personToBeUdated.setSurname(updatedPerson.getSurname());
            personToBeUdated.setAge(updatedPerson.getAge());
            personToBeUdated.setEmail(updatedPerson.getEmail());
    }

    /** метод удаляет пользователя по  его id **/
    public void delete(int id)
    {
        people.removeIf(person -> person.getId() == id);
    }
}
