package org.spike.spikejpa;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class SpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class);
    }

    @Bean
    public CommandLineRunner demo(PersonRepository repository) {
        return (args) -> {

            // fetch all persons
            System.out.println("Persons found with findAll():");
            System.out.println("-------------------------------");

            // Get Persons
            repository.findAll().forEach(person -> {
                System.out.println("Id = " + person.getPersonId());
                System.out.println("First Name = " + person.getFirstName());
                System.out.println("Last Name = " + person.getLastName());
                System.out.println("Address = " + person.getAddress());
                System.out.println("City = " + person.getCity());
                System.out.println();
            });
            System.out.println("");

//            // fetch an individual customer by ID
//            repository.findById(1L)
//                    .ifPresent(person -> {
//                        System.out.println("Customer found with findById(1L):");
//                        System.out.println("--------------------------------");
//                        System.out.println(person.toString());
//                        System.out.println("");
//                    });
//
//            // fetch customers by last name
//            System.out.println("Customer found with findByLastName('Bauer'):");
//            System.out.println("--------------------------------------------");
//            repository.findByLastName("Bauer").forEach(bauer -> {
//                System.out.println(bauer.toString());
//            });
//            // for (Customer bauer : repository.findByLastName("Bauer")) {
//            // 	System.out.println(bauer.toString());
//            // }
            System.out.println("");
        };
    }

}
