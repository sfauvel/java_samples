package org.spike;


import org.junit.Assert;
import org.junit.Test;
import org.spike.mapper.Mapper;
import org.spike.model.Person;
import org.spike.model.PersonDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.spike.mapper.Mapper.mapGeneric;
import static org.spike.mapper.MapperPredicate.NotNull;
import static org.spike.mapper.MapperPredicate.mapGeneric;

/**
 * Create a class to make mapping.
 */
public class ClassMapperTest {

    public static class MapperClass {
        // Mapper don't need to be visible outside.
        private static class InternalMapper<G, S, T> {
            public final BiConsumer<S, T> set;
            public final Function<G, T> get;

            public InternalMapper(BiConsumer<S, T> set, Function<G, T> get) {
                this.set = set;
                this.get = get;
            }

            public void apply(S setterObject, G getterObject) {
                set.accept(setterObject, get.apply(getterObject));
            }

            public static <G, S, T> InternalMapper<G, S, T> mapGeneric(BiConsumer<S, T> set, Function<G, T> get) {
                return new InternalMapper<G, S, T>(set, get);
            }
        }

        private final List<InternalMapper> mappers;

        public static MapperClass init() {
            return new MapperClass(Collections.emptyList());
        }

        private MapperClass(List<InternalMapper> mappers) {
            this.mappers = mappers;
        }
        public <S, G, T> MapperClass with(BiConsumer<S, T> setter, Function<G, T> getter) {
            ArrayList<InternalMapper> mappers = new ArrayList<>(this.mappers);
            mappers.add(new InternalMapper(setter, getter));
            return new MapperClass(mappers);
        }

        public <S, G> void apply(S from, G to) {
            mappers.forEach(m -> m.apply(from, to));
        }
    }

    @Test
    public void should_map_with_a_class_mapper() throws Exception {
        MapperClass mapper = MapperClass.init()
                .with(Person::setFirstName, PersonDao::getFn)
                .with(Person::setAge, PersonDao::getA)
                .with(Person::setName, (PersonDao it) -> it.getFn() + " " + it.getNm());

        PersonDao dao = new PersonDao("Moran", "Bob", null, 25);
        Person person = new Person();
        mapper.apply(person, dao);

        Assert.assertEquals("Bob", person.getFirstName());
        Assert.assertEquals(25, person.getAge());
        Assert.assertEquals("Bob Moran", person.getName());
    }

}