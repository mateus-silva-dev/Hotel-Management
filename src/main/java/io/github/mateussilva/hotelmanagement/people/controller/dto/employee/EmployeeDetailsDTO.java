package io.github.mateussilva.hotelmanagement.people.controller.dto.employee;

import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonDetailsDTO;
import io.github.mateussilva.hotelmanagement.people.domain.enums.StatusEmployee;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeDetailsDTO(
        UUID uuid,
        String registrationCode,
        LocalDate hireDate,
        LocalDate dismissalDate,
        String jobTitle,
        StatusEmployee status,

        PersonDetailsDTO person
) { }
