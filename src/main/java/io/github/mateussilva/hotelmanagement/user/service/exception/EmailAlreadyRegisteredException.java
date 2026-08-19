package io.github.mateussilva.hotelmanagement.user.service.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {

    private static final String EMAIL_ALREADY_REGISTERED = "Este e-mail já está cadastrado";

    public EmailAlreadyRegisteredException() {
        super(EMAIL_ALREADY_REGISTERED);
    }

}
