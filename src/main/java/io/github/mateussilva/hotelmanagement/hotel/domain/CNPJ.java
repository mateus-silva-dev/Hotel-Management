package io.github.mateussilva.hotelmanagement.hotel.domain;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import io.github.mateussilva.hotelmanagement.hotel.domain.exception.InvalidDocumentException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CNPJ {

    private static final CNPJValidator CNPJ_VALIDATOR = new CNPJValidator();

    private String value;

    public CNPJ(String value) {
        if (value == null || value.isBlank())
            throw new InvalidDocumentException("O CNPJ informado é nulo ou vazio");
        try {
            value = value.replaceAll("\\D", "");
            validate(value);
            this.value = value;
        } catch (InvalidStateException e) {
            throw new InvalidDocumentException("CNPJ inválido ou em formato incorreto");
        }
    }

    private void validate(String value) {
        CNPJ_VALIDATOR.assertValid(value);
    }

}
