package io.github.mateussilva.hotelmanagement.hotel.controller.dto;

import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelRating;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelType;

import java.util.UUID;

public record HotelMinDTO(
        UUID uuid,
        String name,
        HotelType type,
        HotelRating rating
) { }
