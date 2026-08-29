package io.github.mateussilva.hotelmanagement.people.repository;

import io.github.mateussilva.hotelmanagement.people.domain.CPF;
import io.github.mateussilva.hotelmanagement.people.domain.Person;
import io.github.mateussilva.hotelmanagement.people.projections.PersonDetailsProjection;
import io.github.mateussilva.hotelmanagement.people.projections.PersonMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT obj FROM Person obj WHERE obj.uuid = :uuid")
    Optional<Person> findEntityByUuid(UUID uuid);

    @Query("""
    SELECT
        obj.uuid                   AS uuid,
        obj.firstName              AS firstName,
        obj.surname                AS surname,
        obj.document.value         AS document,
        obj.birthDate              AS birthDate,
        obj.email.value            AS email,
        obj.phoneNumber            AS phoneNumber,
        obj.mobileNumber           AS mobileNumber
    FROM Person obj
    WHERE obj.uuid = :uuid
    """)
    Optional<PersonDetailsProjection> findDetailsByUuid(UUID uuid);

    @Query("""
    SELECT
        p.uuid                AS uuid,
        p.firstName           AS firstName,
        p.surname             AS surname,
        p.email.value         AS email
    FROM Person p
    WHERE (:firstName IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) 
        AND (:surname IS NULL OR LOWER(p.surname) LIKE LOWER(CONCAT('%', :surname, '%'))) 
        AND (:email IS NULL OR LOWER(p.email) = LOWER(:email))
    """)
    Page<PersonMinProjection> findAllMinWithFilters(
            @Param("firstName") String firstName, @Param("surname") String surname, @Param("email") String email,
            Pageable pageable);

    @Query("SELECT obj FROM Person obj WHERE obj.document = :document")
    Optional<Person> findByDocument(CPF document);
}
