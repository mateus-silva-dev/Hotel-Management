package io.github.mateussilva.hotelmanagement.shared;

import io.github.mateussilva.hotelmanagement.shared.doc.Generated;
import io.github.mateussilva.hotelmanagement.shared.exception.InvalidEmailException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
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


    @Generated
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Email email)) return false;

        return Objects.equals(this.value, email.getValue());
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }
}
