package io.github.mateussilva.hotelmanagement.user.service.exception;

public class DocumentAlreadyRegisteredException extends RuntimeException {

    private static final String DOCUMENT_ALREADY_REGISTERED = "Este documento já está cadastrado";

    public DocumentAlreadyRegisteredException() {
        super(DOCUMENT_ALREADY_REGISTERED);
    }

}
