package io.github.mateussilva.hotelmanagement.user.repository;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.user.domain.CPF;
import io.github.mateussilva.hotelmanagement.user.domain.Person;
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

    @Query("SELECT obj FROM Person obj JOIN FETCH obj.email WHERE obj.uuid = :uuid")
    Optional<Person> findByUuid(UUID uuid);

    boolean existsByEmail(Email email);
    boolean existsByDocument(CPF email);

    @Query("SELECT obj FROM Person obj WHERE " +
            "(:firstName IS NULL OR LOWER(obj.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:surname IS NULL OR LOWER(obj.surname) LIKE LOWER(CONCAT('%', :surname, '%'))) AND " +
            "(:document IS NULL OR obj.document = :document) AND " +
            "(:email IS NULL OR LOWER(obj.email) = LOWER(:email))")
    Page<Person> searchWithFilters(
            @Param("firstName") String firstName, @Param("surname") String surname, @Param("document") CPF document, @Param("email") String email,
            Pageable pageable);
}
