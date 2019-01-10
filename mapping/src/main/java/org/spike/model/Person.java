package org.spike.model;

public class Person {

    private String name;
    private String firstName;
    private String city;
    private int age;

    public Person() {
    }

    public Person(String firstName, String name, String city, int age) {
        this.name = name;
        this.firstName = firstName;
        this.city = city;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
