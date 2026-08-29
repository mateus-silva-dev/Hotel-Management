package io.github.mateussilva.hotelmanagement.hotel.domain.exception;

import io.github.mateussilva.hotelmanagement.shared.exception.BusinessRulesException;

public class InvalidHotelException extends BusinessRulesException {

    public InvalidHotelException(String message) {
        super(message);
    }

}
