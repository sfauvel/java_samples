package org.spike;


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
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.spike.mapper.Mapper.mapGeneric;
import static org.spike.mapper.MapperPredicate.NotNull;
import static org.spike.mapper.MapperPredicate.mapGeneric;

public class GenerateMapperDocumentationTest {

    @Test
    public void should_return_method_name_from_mapping() throws Exception {

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

    /**
     * Call getter and setter of mapper and return method names.
     * @param mapper
     * @return
     */
    private String recordGetterSetter(Mapper mapper) {
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