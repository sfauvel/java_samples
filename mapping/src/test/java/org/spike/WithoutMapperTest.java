package org.spike;


import org.junit.Assert;
import org.junit.Test;
import org.spike.model.Person;
import org.spike.model.PersonDao;

import static org.junit.Assert.assertTrue;
import static org.spike.mapper.MapperPredicate.mapGeneric;

/**
 * Show how to make a mapping in a usual way.
 */
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