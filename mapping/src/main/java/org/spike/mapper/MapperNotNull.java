package org.spike.mapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class MapperNotNull<G, S, T> extends MapperPredicate<G, S, T> {
    public MapperNotNull(BiConsumer<S, T> set, Function<G, T> get) {
        super(set, get, NotNull);
    }
}
