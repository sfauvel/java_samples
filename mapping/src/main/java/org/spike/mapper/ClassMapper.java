package org.spike.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Class defining a mapping from on object to another one.
 */
public class ClassMapper {

    private final List<AttributMapper> mappers;

    public static ClassMapper init() {
        return new ClassMapper(Collections.emptyList());
    }

    private ClassMapper(List<AttributMapper> mappers) {
        this.mappers = mappers;
    }

    public <S, G, T> ClassMapper with(BiConsumer<S, T> setter, Function<G, T> getter) {
        ArrayList<AttributMapper> mappers = new ArrayList<>(this.mappers);
        BiConsumer set = setter;
        Function get = getter;
        mappers.add(new AttributMapper(set, get));
        return new ClassMapper(mappers);
    }

    public <S, G> void apply(S from, G to) {
        mappers.forEach(m -> m.apply(from, to));
    }
}
