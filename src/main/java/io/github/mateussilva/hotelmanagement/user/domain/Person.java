package io.github.mateussilva.hotelmanagement.user.domain;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.shared.doc.Generated;
import io.github.mateussilva.hotelmanagement.user.domain.exception.InvalidPersonException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_person")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
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

    @Column
    private UUID uuid;

    @Column
    private String firstName;

    @Column
    private String surname;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "document"))
    private CPF document;

    @Column
    private LocalDate birthDate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email email;

    @Column
    private String phoneNumber;

    @Column
    private String mobileNumber;

    @Generated
    private Person(String firstName, String surname, CPF document, LocalDate birthDate, Email email, String phoneNumber, String mobileNumber) {
        String cleanedPhoneNumber = cleanNumber(phoneNumber);
        String cleanedMobileNumber = cleanNumber(mobileNumber);

        validateCreation(firstName, surname, document, birthDate, email, cleanedPhoneNumber, cleanedMobileNumber);

        this.uuid = UUID.randomUUID();
        this.firstName = firstName;
        this.surname = surname;
        this.document = document;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = cleanedPhoneNumber;
        this.mobileNumber = cleanedMobileNumber;
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
        String cleanedPhoneNumber = cleanNumber(newPhoneNumber);

        if (isBlank(cleanedPhoneNumber) && this.mobileNumber == null)
            throw new InvalidPersonException(CONTACT_REQUIRED_MESSAGE);

        checkPhoneNumber(cleanedPhoneNumber);
        if (Objects.equals(cleanedPhoneNumber, this.phoneNumber)) return;
        this.phoneNumber = cleanedPhoneNumber;
    }

    public void updateMobileNumber(String newMobileNumber) {
        String cleanedMobileNumber = cleanNumber(newMobileNumber);

        if (isBlank(cleanedMobileNumber) && this.phoneNumber == null)
            throw new InvalidPersonException(CONTACT_REQUIRED_MESSAGE);

        checkMobileNumber(cleanedMobileNumber);
        if (Objects.equals(cleanedMobileNumber, this.mobileNumber)) return;
        this.mobileNumber = cleanedMobileNumber;
    }


    private static void validateCreation(String firstName, String surname, CPF document, LocalDate birthDate, Email email, String phoneNumber, String mobileNumber) {
        requireText(firstName, FIRST_NAME_REQUIRED_MESSAGE, 80);
        requireText(surname, SURNAME_REQUIRED_MESSAGE, 255);
        requireNonNull(document, DOCUMENT_REQUIRED_MESSAGE);
        requireNonNull(birthDate, BIRTH_DATE_REQUIRED_MESSAGE);
        requireNonNull(email, EMAIL_REQUIRED_MESSAGE);

        if (isBlank(phoneNumber) && isBlank(mobileNumber))
            throw new InvalidPersonException(CONTACT_REQUIRED_MESSAGE);

        checkPhoneNumber(phoneNumber);
        checkMobileNumber(mobileNumber);
    }

    private static String cleanNumber(String number) {
        return number != null ? number.replaceAll("\\D", "") : null;
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

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static void checkPhoneNumber(String phoneNumber) {
        if (!isBlank(phoneNumber) && phoneNumber.length() != 10)
            throw new InvalidPersonException(CONTACT_INVALID_FORMAT);
    }

    private static void checkMobileNumber(String mobileNumber) {
        if (!isBlank(mobileNumber) && mobileNumber.length() != 11)
            throw new InvalidPersonException(CONTACT_INVALID_FORMAT);
    }


    @Generated
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Person person)) return false;

        return Objects.equals(getUuid(), person.getUuid());
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }
}
