package org.spike.spikejpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findByLastName(String lastName);

    // When an error on request, application not start
    @Query("SELECT u FROM Person u WHERE u.firstName like '%name%'")
    List<Person> search(@Param("name") String searchName);

}
