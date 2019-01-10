package org.spike.mapper;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class MapperPredicate<G, S, T> extends Mapper<G, S, T> {
    private final Predicate<T> predicat;

    public MapperPredicate(BiConsumer<S, T> set, Function<G, T> get, Predicate<T> predicat) {
        super(set, get);
        this.predicat = predicat;
    }

    public void apply(S setterObject, G getterObject) {
        if (predicat.test(get.apply(getterObject))) {
            super.apply(setterObject, getterObject);
        }
    }

    public static <G, S, T> Mapper<G, S, T> mapGeneric(BiConsumer<S, T> set, Function<G, T> get, Predicate<T> p) {
        return new MapperPredicate<G, S, T>(set, get, p);
    }

    public static final Predicate NotNull = value -> value != null;

}
