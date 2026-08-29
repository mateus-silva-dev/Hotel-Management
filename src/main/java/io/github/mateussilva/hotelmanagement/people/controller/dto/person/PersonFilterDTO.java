package io.github.mateussilva.hotelmanagement.people.controller.dto.person;

import io.github.mateussilva.hotelmanagement.people.domain.CPF;

public record PersonFilterDTO(
        String firstName,
        String surname,
        String email,
        CPF document
) { }
