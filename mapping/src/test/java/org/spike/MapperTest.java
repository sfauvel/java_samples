package org.spike;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.spike.mapper.Mapper;
import org.spike.model.Person;
import org.spike.model.PersonDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.spike.mapper.Mapper.mapGeneric;
import static org.spike.mapper.MapperPredicate.NotNull;
import static org.spike.mapper.MapperPredicate.mapGeneric;

public class MapperTest {

    @Test
    public void should_map_with_a_generic_mapper() throws Exception {

        List<Mapper> mappers = Arrays.asList(
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
    public void should_map_with_predicates() throws Exception {

        List<Mapper> mappers = Arrays.asList(
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