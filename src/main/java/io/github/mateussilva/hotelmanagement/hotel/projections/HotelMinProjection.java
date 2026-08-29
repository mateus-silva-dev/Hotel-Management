package io.github.mateussilva.hotelmanagement.hotel.projections;

import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelRating;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelType;

import java.util.UUID;

public interface HotelMinProjection {
    UUID getUuid();
    String getName();
    HotelType getType();
    HotelRating getRating();
}
