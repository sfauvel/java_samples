package org.spike;


import org.junit.Assert;
import org.junit.Test;
import org.spike.mapper.ClassMapper;
import org.spike.mapper.ClassMapperTyped;
import org.spike.model.Person;
import org.spike.model.PersonDao;

/**
 * Create a class with types to make mapping.
 */
public class ClassMapperTypedTest {

    PersonDao dao = new PersonDao("Moran", "Bob", null, 25);
    Person person = new Person();

    @Test
    public void should_map_attributs_with_different_types() throws Exception {
        ClassMapperTyped<PersonDao, Person> mapper = ClassMapperTyped.<PersonDao, Person>init()
                .with(Person::setFirstName, PersonDao::getFn)
                .with(Person::setAge, PersonDao::getA);
                // Here, it's not possible to give an method on an object that it is not a Person or a PersonDao.

        mapper.apply(person, dao);

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
    }

}