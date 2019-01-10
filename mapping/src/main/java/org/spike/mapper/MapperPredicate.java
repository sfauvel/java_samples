package org.spike.mapper;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A mapper with a predicate that need to be true to set the value.
 * @param <G>
 * @param <S>
 * @param <T>
 */
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

    /** A predefine predicate that check the value is not null. */
    public static final Predicate NotNull = value -> value != null;

}
