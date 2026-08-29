package io.github.mateussilva.hotelmanagement.people.domain.exception;

import io.github.mateussilva.hotelmanagement.shared.exception.BusinessRulesException;

public class InvalidEmployeeException extends BusinessRulesException {

    public InvalidEmployeeException(String message) {
        super(message);
    }

}
