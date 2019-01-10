package org.spike;


import org.junit.Assert;
import org.junit.Before;
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
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.spike.mapper.Mapper.mapGeneric;
import static org.spike.mapper.MapperPredicate.NotNull;
import static org.spike.mapper.MapperPredicate.mapGeneric;

public class TypedMapperTest {

    /**
     * A specific mapper that specify object to use.
     * So, it's not possible to call a method on another object.
     * @param <T>
     */
    class MapperPerson<T> extends Mapper<PersonDao, Person, T> {
        public MapperPerson(BiConsumer<Person, T> set, Function<PersonDao, T> get) {
            super(set, get);
        }
    }

    /** Function define to simplify mapping defintion. */
    public <T> Mapper<PersonDao, Person, T> mapPerson(BiConsumer<Person, T> set, Function<PersonDao, T> get) {
        return new Mapper<PersonDao, Person, T>(set, get);
    }

    @Test
    public void should_map_with_a_typed_mapper() throws Exception {

        // Check object type at compilation.
        // It's not possible to give something else than Person function as first argument.
        List<Mapper> mappers = Arrays.asList(
                mapPerson(Person::setFirstName, PersonDao::getFn),
                mapPerson(Person::setAge, PersonDao::getA)
        );

        PersonDao dao = new PersonDao("Moran", "Bob", null, 25);
        Person person = new Person();
        mappers.forEach(m -> m.apply(person, dao));

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
    }


    @Test
    public void should_map_with_a_getter_define_by_a_lambda() throws Exception {

        List<Mapper> mappers = Arrays.asList(
                mapPerson(Person::setName, it -> it.getFn() + " " + it.getNm())
        );

        PersonDao dao = new PersonDao("Moran", "Bob", null, 25);
        Person person = new Person();
        mappers.forEach(m -> m.apply(person, dao));

        Assert.assertEquals("Bob Moran", person.getName());
    }


}