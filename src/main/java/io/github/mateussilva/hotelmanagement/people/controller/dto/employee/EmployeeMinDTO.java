package io.github.mateussilva.hotelmanagement.people.controller.dto.employee;

import io.github.mateussilva.hotelmanagement.people.domain.enums.StatusEmployee;

import java.util.UUID;

public record EmployeeMinDTO(
        UUID uuid,
        String firstName,
        String surname,
        String registrationCode,
        String jobTitle,
        StatusEmployee status
) { }
