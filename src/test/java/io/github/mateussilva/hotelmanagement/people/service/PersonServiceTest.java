package io.github.mateussilva.hotelmanagement.people.service;

import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonCreateDTO;
import io.github.mateussilva.hotelmanagement.people.domain.CPF;
import io.github.mateussilva.hotelmanagement.people.projections.PersonDetailsProjection;
import io.github.mateussilva.hotelmanagement.people.projections.PersonMinProjection;
import io.github.mateussilva.hotelmanagement.shared.exception.InvalidEmailException;
import io.github.mateussilva.hotelmanagement.shared.exception.ResourceNotFoundException;
import io.github.mateussilva.hotelmanagement.support.DomainAssertions;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonFilterDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonUpdateDTO;
import io.github.mateussilva.hotelmanagement.people.domain.Person;
import io.github.mateussilva.hotelmanagement.people.domain.builder.PersonBuilder;
import io.github.mateussilva.hotelmanagement.people.domain.exception.InvalidPersonException;
import io.github.mateussilva.hotelmanagement.people.mapper.PersonMapper;
import io.github.mateussilva.hotelmanagement.people.repository.PersonRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testing the service layer of the Person class")
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest implements DomainAssertions {

    private static final String NOT_FOUND_MESSAGE = "Recurso não encontrado";
    private static final String EMAIL_INVALID_MESSAGE = "Email inválido";
    private static final String CONTACT_INVALID_FORMAT = "Formato do contato inválido";

    private static final Faker faker = new Faker(Locale.of("pt", "BR"));

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonMapper mapper;

    private final UUID uuidFake = UUID.randomUUID();
    private Person personFake;
    private PersonDetailsProjection projectionDetailsFake;

    @BeforeEach
    void setUp() {
        projectionDetailsFake = mock(PersonDetailsProjection.class);
        personFake = PersonBuilder.aPerson().withUuid(uuidFake).build();
    }

    @Nested
    @DisplayName("People searches")
    class Find {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("Given a valid UUID, it should successfully return a Person")
            void validFindByUuid() {
                when(repository.findDetailsByUuid(uuidFake))
                        .thenReturn(Optional.of(projectionDetailsFake));

                var person = service.findDetailsByUuid(uuidFake);

                assertThat(person).isSameAs(projectionDetailsFake);

                verify(repository).findDetailsByUuid(uuidFake);
            }

            @Test
            @DisplayName("It must return a Page containing all People, with the option to include search filters")
            void validFindAll() {
                Pageable pageable = PageRequest.of(0, 5);

                String name = faker.name().firstName();
                String surname = faker.name().lastName();
                String email = faker.internet().emailAddress();
                CPF document = new CPF(faker.cpf().valid());

                var filterFake = new PersonFilterDTO(name, surname, email, document);

                PersonMinProjection projectionFake = mock(PersonMinProjection.class);

                Page<PersonMinProjection> mockPage = new PageImpl<>(List.of(projectionFake), pageable, 1);

                when(repository.findAllMinWithFilters(name, surname, email, document, pageable)).thenReturn(mockPage);

                Page<PersonMinProjection> resultPage = service.findAllMin(filterFake, pageable);

                assertThat(resultPage).isNotNull().hasSize(1);
                assertThat(resultPage.getContent().getFirst()).isEqualTo(projectionFake);
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("Given an invalid UUID, it should return an empty Optional")
            void invalidFindByUuid() {
                when(repository.findDetailsByUuid(uuidFake)).thenReturn(Optional.empty());

                assertThatException(
                        () -> service.findDetailsByUuid(uuidFake),
                        ResourceNotFoundException.class, NOT_FOUND_MESSAGE
                );
            }

        }

    }

    @Nested
    @DisplayName("Register people")
    class Insert {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("Given a valid Person, it should successfully register it")
            void validRegister() {
                PersonCreateDTO dto = mock(PersonCreateDTO.class);
                Person person = mock(Person.class);

                when(mapper.toEntity(dto)).thenReturn(person);
                when(repository.save(person)).thenReturn(person);

                Person result = service.create(dto);

                assertThat(result).isSameAs(person);

                verify(mapper).toEntity(dto);
                verify(repository).save(person);
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("Given invalid data, an exception must be thrown when attempting to register a person")
            void invalidRegister() {
                PersonCreateDTO dto = mock(PersonCreateDTO.class);

                when(mapper.toEntity(dto)).thenThrow(new InvalidPersonException("Dados inválidos"));

                assertThatThrownBy(() -> service.create(dto)).isInstanceOf(InvalidPersonException.class);

                verify(repository, never()).save(any());
            }

        }

    }

    @Nested
    @DisplayName("Update people")
    class Update {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("Given a valid Person, it should successfully update it")
            void validUpdateWithEmailAndPhonesNumbers() {
                String newEmail = faker.internet().emailAddress();
                String phoneNumber = faker.numerify("##########");
                String mobileNumber = faker.numerify("###########");

                var updateDtoFake = new PersonUpdateDTO(newEmail, phoneNumber, mobileNumber);

                when(repository.findEntityByUuid(uuidFake)).thenReturn(Optional.of(personFake));

                when(repository.save(any(Person.class))).thenAnswer(i -> i.getArgument(0));

                Person result = service.update(uuidFake, updateDtoFake);

                assertThat(result)
                        .isNotNull()
                        .satisfies(p -> {
                            assertThat(p.getUuid()).isEqualTo(uuidFake);
                            assertThat(p.getEmail().getValue()).isEqualTo(newEmail);
                            assertThat(p.getPhoneNumber()).isEqualTo(phoneNumber);
                            assertThat(p.getMobileNumber()).isEqualTo(mobileNumber);
                        });
            }

            @Test
            @DisplayName("Given a valid email, it must update only the email")
            void validUpdateWithEmail() {
                String newEmail = faker.internet().emailAddress();

                var updateDtoFake = new PersonUpdateDTO(newEmail, null, null);

                when(repository.findEntityByUuid(uuidFake)).thenReturn(Optional.of(personFake));

                when(repository.save(any(Person.class))).thenAnswer(i -> i.getArgument(0));

                Person result = service.update(uuidFake, updateDtoFake);

                assertThat(result)
                        .isNotNull()
                        .satisfies(p -> {
                            assertThat(p.getUuid()).isEqualTo(uuidFake);
                            assertThat(p.getEmail().getValue()).isEqualTo(newEmail);
                        });
            }


            @Test
            @DisplayName("Given a valid phone number, it must update only the phone number")
            void validUpdateWithPhoneNumber() {
                String phoneNumber = faker.numerify("##########");

                var updateDtoFake = new PersonUpdateDTO(null, phoneNumber, null);

                when(repository.findEntityByUuid(uuidFake)).thenReturn(Optional.of(personFake));

                when(repository.save(any(Person.class))).thenAnswer(i -> i.getArgument(0));

                Person result = service.update(uuidFake, updateDtoFake);

                assertThat(result)
                        .isNotNull()
                        .satisfies(p -> {
                            assertThat(p.getUuid()).isEqualTo(uuidFake);
                            assertThat(p.getPhoneNumber()).isEqualTo(phoneNumber);
                        });
            }

            @Test
            @DisplayName("Given a valid mobile number, it must update only the mobile number")
            void validUpdateWithMobileNumber() {
                String mobileNumber = faker.numerify("###########");

                var updateDtoFake = new PersonUpdateDTO(null ,null, mobileNumber);

                when(repository.findEntityByUuid(uuidFake)).thenReturn(Optional.of(personFake));

                when(repository.save(any(Person.class))).thenAnswer(i -> i.getArgument(0));

                Person result = service.update(uuidFake, updateDtoFake);

                assertThat(result)
                        .isNotNull()
                        .satisfies(p -> {
                            assertThat(p.getUuid()).isEqualTo(uuidFake);
                            assertThat(p.getMobileNumber()).isEqualTo(mobileNumber);
                        });
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("Given an invalid email, it must throw an exception")
            void invalidUpdateWithEmail() {
                String invalidEmail = "invalidEmail";

                var updateDtoFake = new PersonUpdateDTO(invalidEmail, null, null);

                when(repository.findEntityByUuid(uuidFake)).thenReturn(Optional.of(personFake));

                assertThatException(
                        () -> service.update(uuidFake, updateDtoFake),
                        InvalidEmailException.class, EMAIL_INVALID_MESSAGE
                );

                verify(repository, never()).save(any(Person.class));
            }

            @Test
            @DisplayName("Given an invalid phone number, it must throw an exception")
            void invalidUpdateWithPhoneNumber() {
                String phoneNumber = faker.numerify("#####");

                var updateDtoFake = new PersonUpdateDTO(null, phoneNumber, null);

                when(repository.findEntityByUuid(uuidFake)).thenReturn(Optional.of(personFake));

                assertThatException(
                        () -> service.update(uuidFake, updateDtoFake),
                        InvalidPersonException.class, CONTACT_INVALID_FORMAT
                );

                verify(repository, never()).save(any(Person.class));
            }

            @Test
            @DisplayName("Given an invalid mobile number, it must throw an exception")
            void invalidUpdateWithMobileNumber() {
                String mobileNumber = faker.numerify("#####");

                var updateDtoFake = new PersonUpdateDTO(null, null, mobileNumber);

                when(repository.findEntityByUuid(uuidFake)).thenReturn(Optional.of(personFake));

                assertThatException(
                        () -> service.update(uuidFake, updateDtoFake),
                        InvalidPersonException.class, CONTACT_INVALID_FORMAT
                );

                verify(repository, never()).save(any(Person.class));
            }

        }

    }

    @Nested
    @DisplayName("Find or create people")
    class FindOrCreate {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("Should return an existing Person when document is already registered")
            void findOrCreateExistingPerson() {
                PersonCreateDTO dto = mock(PersonCreateDTO.class);

                when(dto.document()).thenReturn("38497862856");
                when(repository.findByDocument(any(CPF.class)))
                        .thenReturn(Optional.of(personFake));

                Person result = service.findOrCreate(dto);

                assertThat(result).isSameAs(personFake);

                verify(repository).findByDocument(any(CPF.class));
                verify(repository, never()).save(any());
                verify(mapper, never()).toEntity(any());
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("Should create a Person when document is not registered")
            void findOrCreateNewPerson() {
                PersonCreateDTO dto = mock(PersonCreateDTO.class);
                Person newPerson = PersonBuilder.aPerson().build();

                when(dto.document()).thenReturn(newPerson.getDocument().getValue());

                when(repository.findByDocument(any(CPF.class))).thenReturn(Optional.empty());

                when(mapper.toEntity(dto)).thenReturn(newPerson);
                when(repository.save(newPerson)).thenReturn(newPerson);

                Person result = service.findOrCreate(dto);

                assertThat(result).isSameAs(newPerson);

                verify(repository).findByDocument(any(CPF.class));
                verify(mapper).toEntity(dto);
                verify(repository).save(newPerson);
            }

        }

    }
}
