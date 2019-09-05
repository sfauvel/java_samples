package org.spike.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Class defining a mapping from on object to another one.
 * This class is typed so it's not possible to call 'with' with method one wrong object.
 */
public class ClassMapperTyped<G, S> {

    private final List<AttributMapper> mappers;

    public static <G, S> ClassMapperTyped<G, S> init() {
        return new ClassMapperTyped<G, S>(Collections.emptyList());
    }

    private ClassMapperTyped(List<AttributMapper> mappers) {
        this.mappers = mappers;
    }

    public <T> ClassMapperTyped<G, S> with(BiConsumer<S, T> setter, Function<G, T> getter) {
        ArrayList<AttributMapper> mappers = new ArrayList<>(this.mappers);
        BiConsumer set = setter;
        Function get = getter;
        mappers.add(new AttributMapper(set, get));
        return new ClassMapperTyped<G, S>(mappers);
    }

    public <S, G> void apply(S from, G to) {
        mappers.forEach(m -> m.apply(from, to));
    }
}
