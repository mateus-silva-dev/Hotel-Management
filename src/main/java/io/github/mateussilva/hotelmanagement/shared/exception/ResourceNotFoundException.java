package io.github.mateussilva.hotelmanagement.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Recurso não encontrado";

    public ResourceNotFoundException() {
        super(NOT_FOUND);
    }

}
