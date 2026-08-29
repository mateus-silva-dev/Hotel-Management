package io.github.mateussilva.hotelmanagement.hotel.controller.dto;

import io.github.mateussilva.hotelmanagement.hotel.domain.enums.BoardBasis;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelRating;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelType;

public record HotelUpdateDTO(
        String newName,
        String newDescription,
        HotelType newType,
        HotelRating newRating,
        BoardBasis newBoardBasis,
        String newEmail,
        String newPhoneNumber,
        String newMobileNumber
) { }
