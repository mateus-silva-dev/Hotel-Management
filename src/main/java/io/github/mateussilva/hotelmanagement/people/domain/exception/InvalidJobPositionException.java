package io.github.mateussilva.hotelmanagement.people.domain.exception;

import io.github.mateussilva.hotelmanagement.shared.exception.BusinessRulesException;

public class InvalidJobPositionException extends BusinessRulesException {

    public InvalidJobPositionException(String message) {
        super(message);
    }

}
