package io.github.mateussilva.hotelmanagement.people.controller.dto.person;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PersonDTO(
        String uuid,

        @NotBlank(message = "Campo requerido")
        @Size(min = 3, max = 80, message = "O nome deve ter entre 3 e 80 caracteres")
        String firstName,

        @NotBlank(message = "Campo requerido")
        @Size(min = 3, max = 255, message = "O sobrenome deve ter entre 3 e 255 caracteres")
        String surname,

        @NotBlank(message = "Campo requerido")
        @CPF
        String document,

        @NotNull(message = "Campo requerido")
        @PastOrPresent(message = "A data de nascimento deve ser no passado")
        LocalDate birthDate,

        @NotBlank(message = "Campo requerido")
        @Email
        String email,

        @Size(min = 10, max = 10, message = "O número de telefone deve ter 10 digítos")
        String phoneNumber,

        @Size(min = 11, max = 11, message = "O número de celular deve ter 11 digítos")
        String mobileNumber
) { }
