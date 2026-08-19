package io.github.mateussilva.hotelmanagement.user.service.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Recurso não encontrado";

    public ResourceNotFoundException() {
        super(NOT_FOUND);
    }

}
