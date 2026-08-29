package io.github.mateussilva.hotelmanagement.people.repository;

import io.github.mateussilva.hotelmanagement.people.domain.Employee;
import io.github.mateussilva.hotelmanagement.people.domain.enums.StatusEmployee;
import io.github.mateussilva.hotelmanagement.people.projections.EmployeeDetailsProjection;
import io.github.mateussilva.hotelmanagement.people.projections.EmployeeMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT obj FROM Employee obj WHERE obj.uuid = :uuid")
    Optional<Employee> findEntityByUuid(UUID uuid);

    @Query("""
    SELECT
        e.uuid              AS uuid,
        e.registrationCode  AS registrationCode,
        e.hireDate          AS hireDate,
        e.dismissalDate     AS dismissalDate,
        e.status            AS status,
        p.uuid                   AS personUuid,
        p.firstName              AS personFirstName,
        p.surname                AS personSurname,
        p.document.value         AS personDocument,
        p.birthDate              AS personBirthDate,
        p.email.value            AS personEmail,
        p.phoneNumber            AS personPhoneNumber,
        p.mobileNumber           AS personMobileNumber
    FROM Employee e
    JOIN e.person p
    WHERE e.uuid = :uuid
    """)
    Optional<EmployeeDetailsProjection> findDetailsByUuid(UUID uuid);

    @Query(value = """
    SELECT
        e.uuid              AS uuid,
        p.firstName         AS firstName,
        p.surname           AS surname,
        e.registrationCode  AS registrationCode,
        e.status            AS status
    FROM Employee e
    JOIN e.person p
    WHERE (:firstName IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
          AND (:surname IS NULL OR LOWER(p.surname) LIKE LOWER(CONCAT('%', :surname, '%')))
          AND (:registrationCode IS NULL OR e.registrationCode = :registrationCode)
          AND (:status IS NULL OR e.status = :status)
    """)
    Page<EmployeeMinProjection> findAllMinWithFilters(
            @Param("firstName") String firstName, @Param("surname") String surname, @Param("registrationCode") String registrationCode,
            @Param("status")StatusEmployee status, Pageable pageable
    );
}
