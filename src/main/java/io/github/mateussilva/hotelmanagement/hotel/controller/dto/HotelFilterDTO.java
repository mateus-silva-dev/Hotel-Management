package io.github.mateussilva.hotelmanagement.hotel.controller.dto;

import io.github.mateussilva.hotelmanagement.hotel.domain.enums.Amenity;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.BoardBasis;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelRating;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelType;

import java.util.Set;

public record HotelFilterDTO(
        String name,
        HotelType type,
        HotelRating rating,
        BoardBasis boardBasis,
        Set<Amenity> amenities
) { }
