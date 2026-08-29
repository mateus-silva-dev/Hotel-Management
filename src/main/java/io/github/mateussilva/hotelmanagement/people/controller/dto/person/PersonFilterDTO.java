package io.github.mateussilva.hotelmanagement.people.controller.dto.person;

public record PersonFilterDTO(
        String firstName,
        String surname,
        String email
) { }
