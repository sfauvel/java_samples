package org.spike;


import org.junit.Assert;
import org.junit.Test;
import org.spike.mapper.ClassMapper;
import org.spike.mapper.ClassMapperTyped;
import org.spike.model.Person;
import org.spike.model.PersonDao;

/**
 * Create a class to make mapping.
 */
public class ClassMapperTest {

    public void setAge(Integer i) {

    }

    PersonDao dao = new PersonDao("Moran", "Bob", null, 25);
    Person person = new Person();

    @Test
    public void should_map_attributs_with_different_types() throws Exception {
        ClassMapper mapper = ClassMapper.init()
                .with(Person::setFirstName, PersonDao::getFn)
                .with(Person::setAge, PersonDao::getA)
                .with(ClassMapperTest::setAge, PersonDao::getA);


        mapper.apply(person, dao);

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
    }


    @Test
    public void should_map_attributs_with_different_types_classmappertyped() throws Exception {
        ClassMapperTyped<PersonDao, Person> mapper = ClassMapperTyped.<PersonDao, Person>init()
                .with(Person::setFirstName, PersonDao::getFn)
                .with(Person::setAge, PersonDao::getA);


        mapper.apply(person, dao);

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
    }

    @Test
    public void should_map_an_attribut_with_a_transformation() throws Exception {

        ClassMapper mapper = ClassMapper.init()
                .with(Person::setName, (PersonDao it) -> it.getFn() + " " + it.getNm());

        mapper.apply(person, dao);

        Assert.assertEquals("Bob Moran", person.getName());
    }
}