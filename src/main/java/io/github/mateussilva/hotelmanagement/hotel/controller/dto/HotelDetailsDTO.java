package io.github.mateussilva.hotelmanagement.hotel.controller.dto;

import io.github.mateussilva.hotelmanagement.hotel.domain.enums.Amenity;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.BoardBasis;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelRating;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelType;

import java.util.Set;
import java.util.UUID;

public record HotelDetailsDTO(
        UUID uuid,
        String name,
        String description,
        HotelType type,
        HotelRating rating,
        BoardBasis defaultBoardBasis,
        Set<Amenity> amenities,
        String email,
        String phoneNumber,
        String mobileNumber
) { }
