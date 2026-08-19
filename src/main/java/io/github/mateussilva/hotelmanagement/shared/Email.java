package io.github.mateussilva.hotelmanagement.shared;

import io.github.mateussilva.hotelmanagement.shared.exception.InvalidEmailException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    private String value;

    public Email(String value) {
        if (value == null || value.isBlank())
            throw new InvalidEmailException("O email não pode ser nulo ou vazio");

        value = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(value).matches())
            throw new InvalidEmailException("Email inválido");

        this.value = value;
    }

}
