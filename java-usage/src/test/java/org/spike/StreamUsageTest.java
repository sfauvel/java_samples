package org.spike;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class StreamUsageTest {


    @Test
    void should_strip_1() {
        final List<Integer> values = Arrays.asList(0, 0, 0, 1, 2, 0, 1, 0, 0);
        List<Integer> result = values;

        Collections.reverse(result);
        result = result.stream().dropWhile(i -> i == 0).collect(Collectors.toList());

        Collections.reverse(result);
        result = result.stream().dropWhile(i -> i == 0).collect(Collectors.toList());


        System.out.println(result.toString());
        assertArrayEquals(new Integer[]{1, 2, 0, 1}, result.toArray());
    }


    @Test
    void should_strip_2() {
        final List<Integer> values = Arrays.asList(0, 0, 0, 1, 2, 0, 1, 0, 0);
        List<Integer> result = values;

        result = result.stream()
                .dropWhile(i -> i == 0)
                .collect(ArrayList<Integer>::new, (l, v) -> l.add(0, v), (l1, l2) -> l1.addAll(l2))
                .stream()
                .dropWhile(i -> i == 0)
                .collect(ArrayList::new, (l, v) -> l.add(0, v), (l1, l2) -> l1.addAll(l2));


        System.out.println(result.toString());
        assertArrayEquals(new Integer[]{1, 2, 0, 1}, result.toArray());
    }

    @Test
    void should_strip_3() {
        final List<Integer> values = Arrays.asList(0, 0, 0, 1, 2, 0, 1, 0, 0);
        List<Integer> result = values;

        result = result
                .stream().dropWhile(i -> i == 0).collect(toReverseList())
                .stream().dropWhile(i -> i == 0).collect(toReverseList());

        System.out.println(result.toString());
        assertArrayEquals(new Integer[]{1, 2, 0, 1}, result.toArray());
    }

    @Test
    void should_strip_4() {
        final List<Integer> values = Arrays.asList(0, 0, 0, 1, 2, 0, 1, 0, 0);
        List<Integer> result = strip(values, i -> i == 0);

        assertArrayEquals(new Integer[]{1, 2, 0, 1}, result.toArray());
    }

    private static List<Integer> strip(List<Integer> values, Predicate<Integer> predicate) {
        return values
                .stream().dropWhile(predicate).collect(toReverseList())
                .stream().dropWhile(predicate).collect(toReverseList());
    }

    private static Collector<Integer, List, List<Integer>> toReverseList() {
        return new CollectorReverseList();
    }

    class Person {
        String firstname;
        String lastname;
        int age;

        public Person(String firstname, String lastname, int age) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    void understand_collector() {
        final List<Person> people = List.of(
                new Person("aaa", "AAA", 5),
                new Person("bbb", "BBB", 7),
                new Person("ccc", "CCC", 4),
                new Person("ddd", "DDD", 3)
        );
        List<String> calls = new ArrayList<>();
        Collector<Person, List<Person>, List<Person>> collect = new Collector<Person, List<Person>, List<Person>>() {

            @Override
            public Supplier<List<Person>> supplier() {
                calls.add("StreamUsageTest.supplier");
                return () -> {
                    calls.add("StreamUsageTest.supplier >>>");
                    return new ArrayList<Person>();
                };
            }

            @Override
            public BiConsumer<List<Person>, Person> accumulator() {
                calls.add("StreamUsageTest.accumulator");
                return (list, person) -> {
                    calls.add("StreamUsageTest.accumulator >>> " + person.toString());
                    list.add(person);
                };
            }

            @Override
            public BinaryOperator<List<Person>> combiner() {
                calls.add("StreamUsageTest.combiner");
                return (firstList, secondList) -> {
                    calls.add("StreamUsageTest.combiner >>>");
                    firstList.addAll(secondList);
                    return firstList;
                };
            }

            @Override
            public Function<List<Person>, List<Person>> finisher() {
                calls.add("StreamUsageTest.finisher");
                return list -> {
                    calls.add("StreamUsageTest.finisher >>>");
                    return list;
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                calls.add("StreamUsageTest.characteristics");
                calls.add("StreamUsageTest.characteristics >>>");
                return new HashSet<>();
            }
        };

        final List<Person> collect1 = people.stream().collect(collect);
        System.out.println(calls.stream().collect(Collectors.joining("\n")));
        assertEquals(4, collect1.size());
    }

    @Test
    void understand_collector_return_different_type() {
        final List<Person> people = List.of(
                new Person("aaa", "AAA", 5),
                new Person("bbb", "BBB", 7),
                new Person("ccc", "CCC", 4),
                new Person("ddd", "DDD", 3)
        );
        List<String> calls = new ArrayList<>();
        Collector<Person, List<Person>, String> collect = new Collector<Person, List<Person>, String>() {

            @Override
            public Supplier<List<Person>> supplier() {
                calls.add("StreamUsageTest.supplier");
                return () -> {
                    calls.add("StreamUsageTest.supplier >>>");
                    return new ArrayList<Person>();
                };
            }

            @Override
            public BiConsumer<List<Person>, Person> accumulator() {
                calls.add("StreamUsageTest.accumulator");
                return (list, person) -> {
                    calls.add("StreamUsageTest.accumulator >>> " + person.toString());
                    list.add(person);
                    final String content = list.stream().map(p -> p.lastname).collect(Collectors.joining(","));
                    calls.add("StreamUsageTest.accumulator >>> content:" + content);
                };
            }

            @Override
            public BinaryOperator<List<Person>> combiner() {
                calls.add("StreamUsageTest.combiner");
                return (firstList, secondList) -> {
                    calls.add("StreamUsageTest.combiner >>>");
                    final String firstContent = firstList.stream().map(p -> p.lastname).collect(Collectors.joining(","));
                    final String secondContent = secondList.stream().map(p -> p.lastname).collect(Collectors.joining(","));
                    calls.add("StreamUsageTest.combiner >>> " + firstContent + " + " + secondContent);
                    firstList.addAll(secondList);
                    return firstList;
                };
            }

            @Override
            public Function<List<Person>, String> finisher() {
                calls.add("StreamUsageTest.finisher");
                return list -> {
                    calls.add("StreamUsageTest.finisher >>>");
                    return list.stream().map(p -> p.lastname).collect(Collectors.joining(","));
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                calls.add("StreamUsageTest.characteristics");
                calls.add("StreamUsageTest.characteristics >>>");

                return new HashSet<>();
            }
        };

        {
            final String collect1 = people.stream().collect(collect);
            System.out.println(calls.stream().collect(Collectors.joining("\n")));
            assertEquals("AAA,BBB,CCC,DDD", collect1);
        }
        calls.clear();
        System.out.println();
        {
            final String collect1 = people.parallelStream().collect(collect);
            System.out.println(calls.stream().collect(Collectors.joining("\n")));
            assertEquals("AAA,BBB,CCC,DDD", collect1);
        }

    }

    @Test
    void understand_collector_characteristic() {
        final List<Person> people = List.of(
                new Person("aaa", "AAA", 5),
                new Person("bbb", "BBB", 7),
                new Person("ccc", "CCC", 4),
                new Person("ddd", "DDD", 3)
        );
        List<String> calls = new ArrayList<>();
        final Set<Collector.Characteristics> characteristics = new HashSet<>();
        Collector<Person, List<Person>, List<Person>> collect = new Collector<Person, List<Person>, List<Person>>() {

            @Override
            public Supplier<List<Person>> supplier() {
                calls.add("StreamUsageTest.supplier");
                return () -> {
                    calls.add("StreamUsageTest.supplier >>>");
                    return new ArrayList<Person>();
                };
            }

            @Override
            public BiConsumer<List<Person>, Person> accumulator() {
                calls.add("StreamUsageTest.accumulator");
                return (list, person) -> {
                    calls.add("StreamUsageTest.accumulator >>> " + person.toString());
                    list.add(person);
                };
            }

            @Override
            public BinaryOperator<List<Person>> combiner() {
                calls.add("StreamUsageTest.combiner");
                return (firstList, secondList) -> {
                    calls.add("StreamUsageTest.combiner >>>");
                    firstList.addAll(secondList);
                    return firstList;
                };
            }

            @Override
            public Function<List<Person>, List<Person>> finisher() {
                calls.add("StreamUsageTest.finisher");
                return list -> {
                    calls.add("StreamUsageTest.finisher >>>");
                    return list;
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                calls.add("StreamUsageTest.characteristics");
                calls.add("StreamUsageTest.characteristics >>>");

                return characteristics;
            }
        };

        {
            calls.clear();
            characteristics.clear();
            final List<Person> collect1 = people.stream().collect(collect);
            System.out.println(calls.stream().collect(Collectors.joining("\n")));
            assertTrue(calls.contains("StreamUsageTest.finisher >>>"));
        }
        {
            calls.clear();
            characteristics.add(Collector.Characteristics.IDENTITY_FINISH);
            final List<Person> collect1 = people.stream().collect(collect);
            System.out.println(calls.stream().collect(Collectors.joining("\n")));
            assertFalse(calls.contains("StreamUsageTest.finisher >>>"));
        }
        {
            calls.clear();
            characteristics.add(Collector.Characteristics.CONCURRENT); // No real impact
            final List<Person> collect1 = people.parallelStream().collect(collect);
            System.out.println(calls.stream().collect(Collectors.joining("\n")));
            assertFalse(calls.contains("StreamUsageTest.finisher >>>"));
        }

    }


    @Test
    void understand_collector_of() {
        final List<Person> people = List.of(
                new Person("aaa", "AAA", 5),
                new Person("bbb", "BBB", 7),
                new Person("ccc", "CCC", 4),
                new Person("ddd", "DDD", 3)
        );
        Collector<Person, List<Person>, String> collect = Collector.of(
                () -> new ArrayList<Person>(),
                (list, person) -> list.add(person),
                (firstList, secondList) -> secondList.stream().peek(firstList::add).toList(),
                list -> list.stream().map(p -> p.lastname).collect(Collectors.joining(","))
        );

        final String collect1 = people.stream().collect(collect);
        assertEquals("AAA,BBB,CCC,DDD", collect1);
    }
}

class CollectorReverseList implements Collector<Integer, List, List<Integer>> {
    @Override
    public Supplier<List> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List, Integer> accumulator() {
        return (l, v) -> l.add(0, v);
    }

    @Override
    public BinaryOperator<List> combiner() {
        return (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        };
    }

    @Override
    public Function<List, List<Integer>> finisher() {
        return l -> l;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }
}

