package org.spike.domain;

/**
 * Domain objet representing a person.
 *
 */
public class Person {

    private final String firstName;
    private final String lastName;

    /**
     * Constructor of a person.
     * @param firstName
     * @param lastName
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /** First name. */
    public String getFirstName() {
        return firstName;
    }

    /** Last name. */
    public String getLastName() {
        return lastName;
    }

    /**
     * A simple method to show a comment.
     */
    public void simpleMethod() {

    }
}
