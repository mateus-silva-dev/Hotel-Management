package io.github.mateussilva.hotelmanagement.people.controller.dto.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record PersonUpdateDTO(
        @Email
        String newEmail,

        @Size(min = 10, max = 10, message = "O número de telefone deve ter 10 digítos")
        String newPhoneNumber,

        @Size(min = 11, max = 11, message = "O número de celular deve ter 11 digítos")
        String newMobileNumber
) { }
