package io.github.mateussilva.hotelmanagement.people.domain;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import io.github.mateussilva.hotelmanagement.shared.doc.Generated;
import io.github.mateussilva.hotelmanagement.people.domain.exception.InvalidDocumentException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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


    @Generated
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CPF cpf)) return false;

        return Objects.equals(this.value, cpf.getValue());
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }
}
