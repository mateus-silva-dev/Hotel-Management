package io.github.mateussilva.hotelmanagement.people.controller.dto.jobposition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record JobPositionDTO(
        UUID uuid,

        @NotBlank(message = "Campo requerido")
        @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
        String name,

        boolean active
) { }
