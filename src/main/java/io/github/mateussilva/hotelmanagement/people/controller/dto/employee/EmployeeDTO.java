package io.github.mateussilva.hotelmanagement.people.controller.dto.employee;

import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeDTO(
        @NotNull(message = "Campo requerido")
        @Valid
        PersonDTO person,

        @NotNull(message = "Campo requerido")
        LocalDate hireDate,

        @NotNull(message = "Informe um cargo")
        UUID jobPosition,

        @NotNull(message = "Informe o hotel de trabalho do funcionário")
        UUID hotel
) { }
