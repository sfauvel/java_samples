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


    /**
     * Simple method to make a mapping.
     * @param dao
     * @return
     */
    public Person load(PersonDao dao) {
        Person person = new Person();
        person.setName(dao.getNm());
        person.setFirstName(dao.getFn());
        person.setAge(dao.getA());
        return person;
    }

    class MapperPerson<T> extends Mapper<PersonDao, Person, T> {

        public MapperPerson(BiConsumer<Person, T> set, Function<PersonDao, T> get) {
            super(set, get);
        }
    }

    public <T> Mapper<PersonDao, Person, T> mapPerson(BiConsumer<Person, T> set, Function<PersonDao, T> get) {
        return new Mapper<PersonDao, Person, T>(set, get);
    }

    @Test
    public void loadRefacto() throws Exception {

        PersonDao dao = new PersonDao();
        dao.setNm("Moran");
        dao.setFn("Bob");
        dao.setA(25);

        Person person = new Person();

        // Check object type at compilation.
        // It's not possible to give something else than Person function as first argument.
        List<Mapper> mappers = Arrays.asList(
                mapPerson(Person::setFirstName, PersonDao::getFn),
                mapPerson(Person::setAge, PersonDao::getA),
                mapPerson(Person::setName, it -> it.getFn() + " " + it.getNm()));

        mappers.forEach(m -> m.apply(person, dao));

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
        Assert.assertEquals("Bob Moran", person.getName());
    }

    @Test
    public void loadNotNull() throws Exception {

        PersonDao dao = new PersonDao();
        dao.setFn(null);
        dao.setNm(null);
        dao.setCi(null);
        dao.setA(-1);


        Person person = new Person();
        person.setFirstName("Unknown");
        // setName is not call.
        person.setCity("Unknown");
        person.setAge(30);

        List<Mapper> mappers = Arrays.asList(
                //mapGeneric(attribut, /***/with, /***/when),
                mapGeneric(Person::setFirstName, /***/PersonDao::getFn, /***/NotNull),
                mapGeneric(Person::setName, /********/PersonDao::getNm  /***/),
                mapGeneric(Person::setCity, /********/PersonDao::getCi, /***/NotNull),
                mapGeneric(Person::setAge, /*********/PersonDao::getA, /****/age -> age != -1)
        );

        mappers.forEach(m -> m.apply(person, dao));

        Assert.assertEquals("Unknown", person.getFirstName());
        Assert.assertEquals(null, person.getName());
        Assert.assertEquals("Unknown", person.getCity());
        Assert.assertEquals(30, person.getAge());
    }

    @Test
    public void loadRefactoMapGeneric() throws Exception {

        PersonDao dao = new PersonDao();
        dao.setNm("Moran");
        dao.setFn("Bob");
        dao.setA(25);

        Person person = new Person();

        List<Mapper> mappers = Arrays.asList(
                mapGeneric(Person::setFirstName, PersonDao::getFn),
                mapGeneric(Person::setAge, PersonDao::getA),
                mapGeneric(Person::setName, (PersonDao it) -> it.getFn() + " " + it.getNm()));

        mappers.forEach(m -> m.apply(person, dao));

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
        Assert.assertEquals("Bob Moran", person.getName());
    }

    @Test
    public void should_generate_documentation() throws Exception {

        List<Mapper> mappers = Arrays.asList(
                //mapGeneric(attribut, /***/with, /***/when),
                mapGeneric(Person::setFirstName, /***/PersonDao::getFn, /***/NotNull),
                mapGeneric(Person::setName, /********/PersonDao::getNm  /***/),
                mapGeneric(Person::setCity, /********/PersonDao::getCi, /***/NotNull),
                mapGeneric(Person::setAge, /*********/PersonDao::getA, /****/age -> age != -1)
        );


        List<String> results = mappers.stream()
                .map(mapper -> recordGetterSetter(mapper))
                .collect(Collectors.toList());

        assertTrue(results.containsAll(Arrays.asList(
                "getFn -> setFirstName",
                "getNm -> setName",
                "getCi -> setCity",
                "getA -> setAge"
        )));

    }

    private String recordGetterSetter(Mapper mapper) {
        List<String> messages = new ArrayList<>();
        PersonDao mockDao = Mockito.mock(PersonDao.class, new MethoCallRecorder(messages));
        Person mockPerson = Mockito.mock(Person.class, new MethoCallRecorder(messages));

        mapper.get.apply(mockDao);

        try {
            mapper.set.accept(mockPerson, null);
        } catch (Exception e) {
            mapper.set.accept(mockPerson, 0);
        }

        return messages.get(0) + " -> " + messages.get(1);
    }

    private static class MethoCallRecorder implements Answer {
        private final List<String> messages;

        public MethoCallRecorder(List<String> messages) {
            this.messages = messages;
        }

        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
            //System.out.println(invocationOnMock.getMethod().getName());
            messages.add(invocationOnMock.getMethod().getName());
            return null;
        }
    }
}