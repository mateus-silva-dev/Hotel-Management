package io.github.mateussilva.hotelmanagement.people.controller.dto.employee;

import io.github.mateussilva.hotelmanagement.people.domain.enums.StatusEmployee;

public record EmployeeFilterDTO(
        String firstName,
        String surname,
        String registrationCode,
        StatusEmployee status
) { }
