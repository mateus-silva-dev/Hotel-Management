package io.github.mateussilva.hotelmanagement.people.repository;

import io.github.mateussilva.hotelmanagement.people.domain.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {

    @Query("SELECT obj FROM JobPosition obj WHERE obj.uuid = :uuid")
    Optional<JobPosition> findEntityByUuid(UUID uuid);

}
