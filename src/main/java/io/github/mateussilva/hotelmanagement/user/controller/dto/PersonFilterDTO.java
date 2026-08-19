package io.github.mateussilva.hotelmanagement.user.controller.dto;

import io.github.mateussilva.hotelmanagement.user.domain.CPF;

import java.util.UUID;

public record PersonFilterDTO(
        String firstName,
        String surname,
        CPF document,
        String email
) { }
