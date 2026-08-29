package io.github.mateussilva.hotelmanagement.people.projections;

import io.github.mateussilva.hotelmanagement.people.domain.enums.StatusEmployee;

import java.util.UUID;

public interface EmployeeMinProjection {
    UUID getUuid();
    String getFirstName();
    String getSurname();
    String getRegistrationCode();
    String getJobTitle();
    StatusEmployee getStatus();
}
