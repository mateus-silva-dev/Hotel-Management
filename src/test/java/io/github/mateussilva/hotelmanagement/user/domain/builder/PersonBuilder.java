package io.github.mateussilva.hotelmanagement.user.domain.builder;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.user.domain.CPF;
import io.github.mateussilva.hotelmanagement.user.domain.Person;
import jakarta.persistence.*;
import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Getter
public class PersonBuilder {

    private static final Faker faker = new Faker(Locale.of("pt-BR"));

    private Long id;
    private UUID uuid;
    private String firstName;
    private String surname;
    private CPF document;
    private LocalDate birthDate;
    private Email email;
    private String phoneNumber;
    private String mobileNumber;

    private PersonBuilder() {
        this.uuid = UUID.randomUUID();
        this.firstName = faker.name().firstName();
        this.surname = faker.name().nameWithMiddle() + faker.name().lastName();
        this.document = new CPF(faker.cpf().valid());
        this.birthDate = faker.timeAndDate().birthday();
        this.email = new Email(faker.internet().emailAddress());
        this.phoneNumber = faker.numerify("##########");
        this.mobileNumber = faker.numerify("###########");
    }

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }

    public PersonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PersonBuilder withUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public PersonBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PersonBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public PersonBuilder withDocument(CPF document) {
        this.document = document;
        return this;
    }

    public PersonBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PersonBuilder withEmail(Email email) {
        this.email = email;
        return this;
    }

    public PersonBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PersonBuilder withMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public Person build() {
        Person person = Person.of(firstName, surname, document, birthDate, email, phoneNumber, mobileNumber);
        ReflectionTestUtils.setField(person, "id", id);
        ReflectionTestUtils.setField(person, "uuid", uuid);
        return person;
    }

}