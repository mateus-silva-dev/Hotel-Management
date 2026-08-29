package io.github.mateussilva.hotelmanagement.people.controller.dto.person;

import java.time.LocalDate;
import java.util.UUID;

public record PersonDetailsDTO(
        UUID uuid,
        String firstName,
        String surname,
        String document,
        LocalDate birthDate,
        String email,
        String phoneNumber,
        String mobileNumber
) { }
