package io.github.mateussilva.hotelmanagement.people.domain;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.support.DomainAssertions;
import io.github.mateussilva.hotelmanagement.people.domain.builder.PersonBuilder;
import io.github.mateussilva.hotelmanagement.people.domain.exception.InvalidPersonException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing the business rules of the Person class")
public class PersonTest implements DomainAssertions {

    private static final Faker faker = new Faker(Locale.of("pt-BR"));

    private Person person;
    private PersonBuilder builder;

    @BeforeEach
    void setUp() {
        builder = PersonBuilder.aPerson();
        person = builder.build();
    }

    private static final String FIRST_NAME_REQUIRED_MESSAGE = "Um nome deve ser informado";
    private static final String SURNAME_REQUIRED_MESSAGE = "Um sobrenome deve ser informado";
    private static final String SIZE = "O nome deve ter entre %d e %d caracteres";
    private static final String DOCUMENT_REQUIRED_MESSAGE = "Um documento deve ser informado";
    private static final String BIRTH_DATE_REQUIRED_MESSAGE = "A data de nascimento deve ser informada";
    private static final String EMAIL_REQUIRED_MESSAGE = "Um email deve ser informado";
    private static final String CONTACT_REQUIRED_MESSAGE = "Informe um número de telefone ou celular";
    private static final String CONTACT_INVALID_FORMAT = "Formato do contato inválido";

    @Nested
    @DisplayName("Person")
    class Create {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("A Person must be created when the data is valid.")
            void valid() {
                assertEntityState(person, Map.of(
                        "firstName", builder.getFirstName(),
                        "surname", builder.getSurname(),
                        "document", builder.getDocument(),
                        "birthDate", builder.getBirthDate(),
                        "email", builder.getEmail(),
                        "phoneNumber", builder.getPhoneNumber(),
                        "mobileNumber", builder.getMobileNumber()
                ));
                assertNotNull(person.getUuid());

                person = PersonBuilder.aPerson().withPhoneNumber("1133334444").withMobileNumber(null).build();
                assertThat(person).isNotNull();

                person = PersonBuilder.aPerson().withPhoneNumber(null).withMobileNumber("11999999999").build();
                assertThat(person).isNotNull();
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("A Person must not be created when the name is invalid.")
            void invalidName() {
                assertAll(
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withFirstName(null).build(),
                                InvalidPersonException.class, FIRST_NAME_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withFirstName("").build(),
                                InvalidPersonException.class, FIRST_NAME_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withFirstName("An").build(),
                                InvalidPersonException.class, SIZE.formatted(3, 80)),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withFirstName("A".repeat(90)).build(),
                                InvalidPersonException.class, SIZE.formatted(3, 80))
                );
            }

            @Test
            @DisplayName("A Person must not be created when the surname is invalid.")
            void invalidSurname() {
                assertAll(
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withSurname(null).build(),
                                InvalidPersonException.class, SURNAME_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withSurname("").build(),
                                InvalidPersonException.class, SURNAME_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withSurname("An").build(),
                                InvalidPersonException.class, SIZE.formatted(3, 255)),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withSurname("A".repeat(257)).build(),
                                InvalidPersonException.class, SIZE.formatted(3, 255))
                );
            }

            @Test
            @DisplayName("A Person must not be created when the document is invalid.")
            void invalidDocument() {
                assertThatException(
                                () -> PersonBuilder.aPerson().withDocument(null).build(),
                                InvalidPersonException.class, DOCUMENT_REQUIRED_MESSAGE
                );
            }

            @Test
            @DisplayName("A Person must not be created when the birthdate is invalid.")
            void invalidBirthDate() {
                assertThatException(
                        () -> PersonBuilder.aPerson().withBirthDate(null).build(),
                        InvalidPersonException.class, BIRTH_DATE_REQUIRED_MESSAGE
                );
            }

            @Test
            @DisplayName("A Person must not be created when the email is invalid.")
            void invalidEmail() {
                assertThatException(
                        () -> PersonBuilder.aPerson().withEmail(null).build(),
                        InvalidPersonException.class, EMAIL_REQUIRED_MESSAGE
                );
            }

            @Test
            @DisplayName("A Person must not be created when the phone and mobile number is invalid.")
            void invalidPhoneAndMobileNumber() {
                assertAll(
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber(null).withMobileNumber(null).build(),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber("").withMobileNumber("").build(),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber("").withMobileNumber(null).build(),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber(null).withMobileNumber("").build(),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber("123").withMobileNumber("456").build(),
                                InvalidPersonException.class, CONTACT_INVALID_FORMAT)
                );
            }

            @Test
            @DisplayName("A Person must not be created when the phone number is invalid.")
            void invalidPhoneNumber() {
                assertAll(
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber("117777777").withMobileNumber(null).build(),
                                InvalidPersonException.class, CONTACT_INVALID_FORMAT),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber("texto").withMobileNumber(null).build(),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE)
                );
            }

            @Test
            @DisplayName("A Person must not be created when the mobile number is invalid.")
            void invalidMobileNumber() {
                assertAll(
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber(null).withMobileNumber("1199999999").build(),
                                InvalidPersonException.class, CONTACT_INVALID_FORMAT),
                        () -> assertThatException(
                                () -> PersonBuilder.aPerson().withPhoneNumber(null).withMobileNumber("119999999").build(),
                                InvalidPersonException.class, CONTACT_INVALID_FORMAT)
                );
            }

        }

    }

    @Nested
    @DisplayName("Update Person")
    class Update {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("A Person must be updated when the data is valid.")
            void validUpdate() {
                String newEmail = faker.internet().emailAddress();
                Email newEmailObject = new Email(newEmail);

                String phoneNumber = faker.numerify("##########");
                String mobileNumber = faker.numerify("###########");

                Person personWithMobile = PersonBuilder.aPerson().withPhoneNumber("1144448888").withMobileNumber("11999999999").build();
                Person personWithPhone = PersonBuilder.aPerson().withPhoneNumber("1144448888").withMobileNumber("11999999999").build();

                assertAll(
                        () -> assertUpdateWorkflow(person::updateEmail, person::getEmail, newEmailObject, newEmailObject),
                        () -> assertUpdateWorkflow(person::updatePhoneNumber, person::getPhoneNumber, phoneNumber, phoneNumber),
                        () -> assertUpdateWorkflow(person::updateMobileNumber, person::getMobileNumber, mobileNumber, mobileNumber),

                        () -> assertUpdateWorkflow(person::updatePhoneNumber, person::getPhoneNumber, "(11) 5555-6666", "1155556666"),
                        () -> assertUpdateWorkflow(person::updateMobileNumber, person::getMobileNumber, "(11) 98888-7777", "11988887777"),

                        () -> {
                            personWithMobile.updatePhoneNumber(null);
                            assertNull(personWithMobile.getPhoneNumber());
                        },
                        () -> {
                            personWithPhone.updateMobileNumber(null);
                            assertNull(personWithPhone.getMobileNumber());
                        }
                );
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("A Person must not be updated when the phone number is valid.")
            void invalidPhoneNumber() {
                Person personWithoutMobile = PersonBuilder.aPerson().withPhoneNumber("1144448888").withMobileNumber(null).build();
                Person personWithMobile = PersonBuilder.aPerson().withPhoneNumber("1144448888").withMobileNumber("11999999999").build();

                assertAll(
                        () -> assertThatException(
                                () -> personWithoutMobile.updatePhoneNumber(null),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> personWithoutMobile.updatePhoneNumber(""),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE),

                        () -> assertThatException(
                                () -> personWithoutMobile.updatePhoneNumber(faker.numerify("#####")),
                                InvalidPersonException.class, CONTACT_INVALID_FORMAT),
                        () -> assertThatException(
                                () -> personWithMobile.updatePhoneNumber(faker.numerify("#####")),
                                InvalidPersonException.class, CONTACT_INVALID_FORMAT)
                );
            }

            @Test
            @DisplayName("A Person must not be updated when the mobile number is valid.")
            void invalidMobileNumber() {
                Person personWithoutPhone = PersonBuilder.aPerson().withPhoneNumber(null).withMobileNumber("11999999999").build();
                Person personWithPhone = PersonBuilder.aPerson().withPhoneNumber("1144448888").withMobileNumber("11999999999").build();

                assertAll(
                        () -> assertThatException(
                                () -> personWithoutPhone.updateMobileNumber(null),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE),
                        () -> assertThatException(
                                () -> personWithoutPhone.updateMobileNumber(""),
                                InvalidPersonException.class, CONTACT_REQUIRED_MESSAGE),

                        () -> assertThatException(
                                () -> personWithoutPhone.updateMobileNumber(faker.numerify("#####")),
                                InvalidPersonException.class, CONTACT_INVALID_FORMAT),
                        () -> assertThatException(
                                () -> personWithPhone.updateMobileNumber(faker.numerify("#####")),
                                InvalidPersonException.class, CONTACT_INVALID_FORMAT)
                );
            }

        }

    }

}