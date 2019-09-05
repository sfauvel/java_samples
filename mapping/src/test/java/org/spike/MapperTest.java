package org.spike;


import org.junit.Assert;
import org.junit.Test;
import org.spike.mapper.AttributMapper;
import org.spike.model.Person;
import org.spike.model.PersonDao;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.spike.mapper.AttributMapper.mapGeneric;
import static org.spike.mapper.MapperPredicate.NotNull;
import static org.spike.mapper.MapperPredicate.mapGeneric;

public class MapperTest {

    @Test
    public void should_map_with_a_generic_mapper() throws Exception {

        List<AttributMapper> mappers = Arrays.asList(
                mapGeneric(Person::setFirstName, PersonDao::getFn),
                mapGeneric(Person::setAge, PersonDao::getA),
                mapGeneric(Person::setName, (PersonDao it) -> it.getFn() + " " + it.getNm()));

        PersonDao dao = new PersonDao("Moran", "Bob", null, 25);
        Person person = new Person();
        mappers.forEach(m -> m.apply(person, dao));

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
        Assert.assertEquals("Bob Moran", person.getName());
    }


    @Test
    public void should_not_map_when_condition_is_not_valid() throws Exception {

        List<AttributMapper> mappers = Arrays.asList(
                mapGeneric(Person::setFirstName, PersonDao::getFn, NotNull),
                mapGeneric(Person::setName, PersonDao::getNm));

        PersonDao dao = new PersonDao("Moran", null, null, 25);
        Person person = new Person("?", "?", "?", 0);

        mappers.forEach(m -> m.apply(person, dao));

        Assert.assertEquals("Moran", person.getName());
        Assert.assertEquals("?", person.getFirstName());
    }


    @Test
    public void should_map_with_predicates() throws Exception {

        List<AttributMapper> mappers = Arrays.asList(
                //mapGeneric(attribut, /***/with, /***/when),
                mapGeneric(Person::setFirstName, /***/PersonDao::getFn, /***/NotNull),
                mapGeneric(Person::setName, /********/PersonDao::getNm  /***/),
                mapGeneric(Person::setCity, /********/PersonDao::getCi, /***/NotNull),
                mapGeneric(Person::setAge, /*********/PersonDao::getA, /****/age -> age != -1)
        );

        PersonDao dao = new PersonDao(null, null, null, -1);
        Person person = new Person("Unknown", null, "Unknown", 30);
        mappers.forEach(m -> m.apply(person, dao));

        Assert.assertEquals("Unknown", person.getFirstName());
        Assert.assertEquals(null, person.getName());
        Assert.assertEquals("Unknown", person.getCity());
        Assert.assertEquals(30, person.getAge());
    }

}