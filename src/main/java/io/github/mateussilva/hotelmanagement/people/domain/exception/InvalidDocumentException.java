package io.github.mateussilva.hotelmanagement.people.domain.exception;

import io.github.mateussilva.hotelmanagement.shared.exception.BusinessRulesException;

public class InvalidDocumentException extends BusinessRulesException {

    public InvalidDocumentException(String message) {
        super(message);
    }

}
