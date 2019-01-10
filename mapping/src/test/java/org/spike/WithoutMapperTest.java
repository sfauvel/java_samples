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
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.spike.mapper.Mapper.mapGeneric;
import static org.spike.mapper.MapperPredicate.NotNull;
import static org.spike.mapper.MapperPredicate.mapGeneric;

public class WithoutMapperTest {


    /**
     * Simple method that make a mapping.
     * @param dao
     * @return
     */
    public Person load(PersonDao dao) {
        Person person = new Person();
        person.setName(dao.getFn() + " " + dao.getNm());
        person.setFirstName(dao.getFn());
        person.setAge(dao.getA());
        return person;
    }


    @Test
    public void should_map_with_a_typed_mapper() throws Exception {

        PersonDao dao = new PersonDao("Moran", "Bob", null, 25);

        Person person = load(dao);

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
        Assert.assertEquals("Bob Moran", person.getName());
    }


}