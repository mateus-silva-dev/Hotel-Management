package io.github.mateussilva.hotelmanagement.people.controller.dto.person;

import java.util.UUID;

public record PersonMinDTO(
        UUID uuid,
        String firstName,
        String surname,
        String email
) { }
