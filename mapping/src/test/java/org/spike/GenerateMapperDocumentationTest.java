package org.spike;


import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.spike.mapper.AttributMapper;
import org.spike.mapper.MapperPredicate;
import org.spike.model.Person;
import org.spike.model.PersonDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.spike.mapper.AttributMapper.mapGeneric;
import static org.spike.mapper.MapperPredicate.NotNull;
import static org.spike.mapper.MapperPredicate.mapGeneric;

/**
 * Generate mapping documentation calling getter and setter on object mocked with Mockito.
 *
 */
public class GenerateMapperDocumentationTest {



    @Test
    public void should_return_method_name_from_mapping() throws Exception {

        List<AttributMapper> mappers = Arrays.asList(
                //mapGeneric(attribut,           /***/with,             /***/when),
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


    @Test
    public void should_return_getter_not_matching_predicate() throws Exception {

        List<MapperPredicate> mappers = Arrays.asList(
                //mapGeneric(attribut, /***/with, /***/when),
                mapGeneric(Person::setName, /********/PersonDao::getNm, /***/NotNull),
                mapGeneric(Person::setFirstName, /***/PersonDao::getFn, /***/NotNull),
                mapGeneric(Person::setCity, /********/PersonDao::getCi, /***/NotNull),
                mapGeneric(Person::setAge, /*********/PersonDao::getA, /****/age -> age != -1)
        );

        PersonDao dao = new PersonDao("Moran", "Bob", null, -1);

        List<String> unvalidField = recordUnvalidField(mappers, dao);

        assertEquals(2, unvalidField.size());
        assertTrue(unvalidField.containsAll(Arrays.asList(
                "getCi",
                "getA"
        )));

    }

    /**
     * Record all field mapped and which is invalid according to validation method.
     * @param mappers
     * @param dao
     * @return
     */
    private List<String> recordUnvalidField(List<MapperPredicate> mappers, PersonDao dao) {
        MethodCallRecorder callRecorder = new MethodCallRecorder();
        PersonDao mockDao = Mockito.mock(PersonDao.class, callRecorder);
        mappers.stream()
            .filter(m -> !m.isValid(dao))
            .forEach(m -> m.get.apply(mockDao));
        return callRecorder.messages;
    }

    /**
     * Call getter and setter of mapper and return method names.
     * @param mapper
     * @return
     */
    private String recordGetterSetter(AttributMapper mapper) {
        MethodCallRecorder callRecorder = new MethodCallRecorder();
        PersonDao mockDao = Mockito.mock(PersonDao.class, callRecorder);
        Person mockPerson = Mockito.mock(Person.class, callRecorder);

        mapper.get.apply(mockDao);

        try {
            mapper.set.accept(mockPerson, null);
        } catch (Exception e) {
            mapper.set.accept(mockPerson, 0);
        }

        return callRecorder.call(0) + " -> " + callRecorder.call(1);
    }

    /**
     * Answer that record method name called.
     */
    private static class MethodCallRecorder implements Answer {
        private final List<String> messages = new ArrayList<>();

        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
            messages.add(invocationOnMock.getMethod().getName());
            return null;
        }

        public String call(int callNumber) {
            return messages.get(callNumber);
        }
    }
}