package spike;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.*;
import java.util.List;

public class ConnectJpa {
    public static void main(String[] args) throws SQLException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        PersonService personService = context.getBean(PersonService.class);

        // Get Persons
        List<Person> persons = personService.listPersons();

        for (Person person : persons) {
            System.out.println("Id = " + person.getPersonId());
            System.out.println("First Name = " + person.getFirstName());
            System.out.println("Last Name = " + person.getLastName());
            System.out.println("Address = " + person.getAddress());
            System.out.println("City = " + person.getCity());
            System.out.println();
        }
        context.close();

    }

//    private void writeResultSet(ResultSet resultSet) throws SQLException {
//        // ResultSet is initially before the first data set
//        while (resultSet.next()) {
//            // It is possible to get the columns via name
//            // also possible to get the columns via the column number
//            // which starts at 1
//            // e.g. resultSet.getSTring(2);
//            displayFields(resultSet, "PersonId", "FirstName", "LastName", "Address", "City");
//
//        }
//    }

//    private void displayFields(ResultSet resultSet, String... fields) throws SQLException {
//        for (String field : fields) {
//            displayField(field, resultSet);
//        }
//    }
//
//    private void displayField(String field, ResultSet resultSet) throws SQLException {
//        System.out.println(field + ":" + resultSet.getString(field));
//    }

}
