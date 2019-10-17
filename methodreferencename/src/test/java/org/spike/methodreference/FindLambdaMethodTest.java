package org.spike.methodreference;

import org.junit.jupiter.api.Test;
import org.spike.methodreference.FindLambdaMethod.SerializableConsumer;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.spike.methodreference.FindLambdaMethod.getName;

/**
 * https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.16
 * https://stackoverflow.com/questions/22807912/how-to-serialize-a-lambda
 * https://stackoverflow.com/questions/51070344/strange-java-cast-syntax-using
 */
public class FindLambdaMethodTest {

    @Test
    public void should_find_lambda_name() {

        SerializableConsumer<FindLambdaMethodTest> myConsumer = this::myConsumer;
        assertEquals("myConsumer", getName(myConsumer));

    }

    @Test
    public void should_find_lambda_name_in_a_list() {

        List<SerializableConsumer<FindLambdaMethodTest>> consumers = new ArrayList<>();
        consumers.add(this::myConsumer);

        assertEquals("myConsumer", getName(consumers.get(0)));

    }

    @Test
    public void should_find_lambda_biconsumer_name() {


        List<FindLambdaMethod.SerializableBiConsumer<FindLambdaMethodTest, String>> consumers = new ArrayList<>();
        consumers.add(this::myByConsumer);

        assertEquals("myByConsumer", getName(consumers.get(0)));

    }

    @Test
    public void should_find_lambda_name_with_simple_consumer_casted_before_call() {

        SerializableConsumer<FindLambdaMethodTest> myConsumer = this::myConsumer;
        assertEquals("myConsumer", getName(myConsumer));

        assertEquals("myConsumer", getName((SerializableConsumer<FindLambdaMethodTest>) this::myConsumer));

    }

    @Test
    public void should_find_lambda_name_with_simple_consumer_casted_after_call() {

        assertEquals("myConsumer", getName(this::myConsumer));

    }


    @Test
    public void should_find_lambda_name_in_a_list_with_simple_call() {

        List<SerializableConsumer<FindLambdaMethodTest>> consumers = new ArrayList<>();
        consumers.add(this::myConsumer);

        assertEquals("myConsumer", getName(consumers.get(0)));

    }

    /// method to be used as a Consumer.
    private void myConsumer(FindLambdaMethodTest findLambdaMethod) {
    }

    /// method to be used as a ByConsumer.
    private void myByConsumer(FindLambdaMethodTest findLambdaMethod, String name) {
    }

}
