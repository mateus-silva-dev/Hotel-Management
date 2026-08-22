package io.github.mateussilva.hotelmanagement.user.domain.exception;

import io.github.mateussilva.hotelmanagement.shared.exception.BusinessRulesException;

public class InvalidPersonException extends BusinessRulesException {

    public InvalidPersonException(String message) {
        super(message);
    }

}
