package io.github.mateussilva.hotelmanagement.people.repository;

import io.github.mateussilva.hotelmanagement.people.domain.EmployeeRegistrationSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRegistrationSequenceRepository extends JpaRepository<EmployeeRegistrationSequence, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s
        FROM EmployeeRegistrationSequence s
        WHERE s.hotelId = :hotelId
          AND s.registrationYear = :registrationYear
    """)
    Optional<EmployeeRegistrationSequence> findForUpdate(
            @Param("hotelId") Long hotelId, @Param("registrationYear") Integer year
    );
}
