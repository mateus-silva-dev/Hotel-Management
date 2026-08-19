package io.github.mateussilva.hotelmanagement.user.domain;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.user.domain.exception.InvalidPersonException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_person")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Person {

    private static final String FIRST_NAME_REQUIRED_MESSAGE = "Um nome deve ser informado";
    private static final String SURNAME_REQUIRED_MESSAGE = "Um sobrenome deve ser informado";
    private static final String SIZE = "O nome deve ter entre %d e %d caracteres";
    private static final String DOCUMENT_REQUIRED_MESSAGE = "Um documento deve ser informado";
    private static final String BIRTH_DATE_REQUIRED_MESSAGE = "A data de nascimento deve ser informada";
    private static final String EMAIL_REQUIRED_MESSAGE = "Um email deve ser informado";
    private static final String CONTACT_REQUIRED_MESSAGE = "Informe um número de telefone ou celular";
    private static final String CONTACT_INVALID_FORMAT = "Formato do contato inválido";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID uuid;

    private String firstName;

    private String surname;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "document", unique = true))
    private CPF document;

    private LocalDate birthDate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", unique = true))
    private Email email;

    private String phoneNumber;

    private String mobileNumber;


    private Person(String firstName, String surname, CPF document, LocalDate birthDate, Email email, String phoneNumber, String mobileNumber) {
        validateCreation(firstName, surname, document, birthDate, email, phoneNumber, mobileNumber);
        this.uuid = UUID.randomUUID();
        this.firstName = firstName;
        this.surname = surname;
        this.document = document;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber.replaceAll("\\D", "");
        this.mobileNumber = mobileNumber.replaceAll("\\D", "");
    }

    public static Person of(String firstName, String surname, CPF document, LocalDate birthDate, Email email, String phoneNumber, String mobileNumber) {
        return new Person(firstName, surname, document, birthDate, email, phoneNumber, mobileNumber);
    }


    public void updateEmail(Email newEmail) {
        requireNonNull(newEmail, EMAIL_REQUIRED_MESSAGE);

        if (Objects.equals(newEmail, this.email)) return;
        this.email = newEmail;
    }

    public void updatePhoneNumber(String newPhoneNumber) {
        if (!isBlank(newPhoneNumber) && newPhoneNumber.length() != 10)
            throw new InvalidPersonException(CONTACT_INVALID_FORMAT);

        if (Objects.equals(newPhoneNumber, this.phoneNumber)) return;
        this.phoneNumber = newPhoneNumber;
    }

    public void updateMobileNumber(String newMobileNumber) {
        if (!isBlank(newMobileNumber) && newMobileNumber.length() != 11)
            throw new InvalidPersonException(CONTACT_INVALID_FORMAT);

        if (Objects.equals(newMobileNumber, this.mobileNumber)) return;
        this.mobileNumber = newMobileNumber;
    }


    private static void validateCreation(String firstName, String surname, CPF document, LocalDate birthDate, Email email, String phoneNumber, String mobileNumber) {
        requireText(firstName, FIRST_NAME_REQUIRED_MESSAGE, 80);
        requireText(surname, SURNAME_REQUIRED_MESSAGE, 255);
        requireNonNull(document, DOCUMENT_REQUIRED_MESSAGE);
        requireNonNull(birthDate, BIRTH_DATE_REQUIRED_MESSAGE);
        requireNonNull(email, EMAIL_REQUIRED_MESSAGE);
        requireAtLeastOneContactNumber(phoneNumber, mobileNumber);
    }

    private static void requireText(String value, String message, int maxLength) {
        if (isBlank(value))
            throw new InvalidPersonException(message);
        if (value.length() < 3 || value.length() > maxLength)
            throw new InvalidPersonException(SIZE.formatted(3, maxLength));
    }

    private static void requireNonNull(Object value, String message) {
        if (value == null)
            throw new InvalidPersonException(message);
    }

    private static void requireAtLeastOneContactNumber(String phoneNumber, String mobileNumber) {
        if (isBlank(phoneNumber) && isBlank(mobileNumber))
            throw new InvalidPersonException(CONTACT_REQUIRED_MESSAGE);

        if (!isBlank(phoneNumber) && phoneNumber.length() != 10)
            throw new InvalidPersonException(CONTACT_INVALID_FORMAT);

        if (!isBlank(mobileNumber) && mobileNumber.length() != 11)
            throw new InvalidPersonException(CONTACT_INVALID_FORMAT);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }


    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Person person)) return false;

        return Objects.equals(getUuid(), person.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }
}
