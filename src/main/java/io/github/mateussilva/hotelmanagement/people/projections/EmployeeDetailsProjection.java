package io.github.mateussilva.hotelmanagement.people.projections;

import io.github.mateussilva.hotelmanagement.people.domain.enums.StatusEmployee;

import java.time.LocalDate;
import java.util.UUID;

public interface EmployeeDetailsProjection {
    UUID getUuid();
    String getRegistrationCode();
    LocalDate getHireDate();
    LocalDate getDismissalDate();
    StatusEmployee getStatus();
    String getJobTitle();

    UUID getPersonUuid();
    String getPersonFirstName();
    String getPersonSurname();
    String getPersonDocument();
    LocalDate getPersonBirthDate();
    String getPersonEmail();
    String getPersonPhoneNumber();
    String getPersonMobileNumber();
}
