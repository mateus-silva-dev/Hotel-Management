package io.github.mateussilva.hotelmanagement.user.domain;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import io.github.mateussilva.hotelmanagement.user.domain.exception.InvalidDocumentException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CPF {

    private static final CPFValidator CPF_VALIDATOR = new CPFValidator();

    private String value;

    public CPF(String value) {
        if (value == null || value.isBlank())
            throw new InvalidDocumentException("O CPF informado é nulo ou vazio");
        try {
            value = value.replaceAll("\\D", "");
            validate(value);
            this.value = value;
        } catch (InvalidStateException e) {
            throw new InvalidDocumentException("CPF inválido ou em formato incorreto");
        }
    }

    private void validate(String value) {
        CPF_VALIDATOR.assertValid(value);
    }

}
