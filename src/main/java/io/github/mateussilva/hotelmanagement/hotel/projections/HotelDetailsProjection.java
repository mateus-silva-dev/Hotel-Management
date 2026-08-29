package io.github.mateussilva.hotelmanagement.hotel.projections;

import io.github.mateussilva.hotelmanagement.hotel.domain.enums.Amenity;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.BoardBasis;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelRating;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelType;
import io.github.mateussilva.hotelmanagement.shared.Email;

import java.util.Set;
import java.util.UUID;

public interface HotelDetailsProjection {
    UUID getUuid();
    String getName();
    String getDescription();
    HotelType getType();
    HotelRating getRating();
    BoardBasis getDefaultBoardBasis();
    Set<Amenity> getAmenities();
    Email getEmail();
    String getPhoneNumber();
    String getMobileNumber();
}
