package io.github.mateussilva.hotelmanagement.hotel.domain.exception;

public class InvalidDocumentException extends RuntimeException {
    public InvalidDocumentException(String message) {
        super(message);
    }
}
