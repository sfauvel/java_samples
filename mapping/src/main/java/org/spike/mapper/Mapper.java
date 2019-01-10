package org.spike.mapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Class that apply a setter with the return value of a getter.
 * @param <G> Class of the getter.
 * @param <S> Class of the setter.
 * @param <T> Type of the value to transfert.
 */
public class Mapper<G, S, T> {
    public final BiConsumer<S, T> set;
    public final Function<G, T> get;

    public Mapper(BiConsumer<S, T> set, Function<G, T> get) {
        this.set = set;
        this.get = get;
    }

    public void apply(S setterObject, G getterObject) {
        set.accept(setterObject, get.apply(getterObject));
    }

    public static <G, S, T> Mapper<G, S, T> mapGeneric(BiConsumer<S, T> set, Function<G, T> get) {
        return new Mapper<G, S, T>(set, get);
    }
}
