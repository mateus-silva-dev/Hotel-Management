package io.github.mateussilva.hotelmanagement.shared.exception;

public class InvalidEmailException extends BusinessRulesException {

    public InvalidEmailException(String message) {
        super(message);
    }

}
