package io.github.mateussilva.hotelmanagement.shared.handlers;

import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonDTO;
import io.github.mateussilva.hotelmanagement.shared.exception.BusinessRulesException;
import io.github.mateussilva.hotelmanagement.shared.exception.ResourceNotFoundException;
import io.github.mateussilva.hotelmanagement.people.controller.PersonController;
import io.github.mateussilva.hotelmanagement.people.mapper.PersonMapper;
import io.github.mateussilva.hotelmanagement.people.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@Import(ControllerExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Controller Exception Handler Tests")
public class ControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService service;

    @MockitoBean
    private PersonMapper mapper;

    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
    }

    private static final String VALID_PERSON_JSON = """
            {
                "firstName": "Carla",
                "surname": "Batista Silva",
                "document": "65824217866",
                "birthDate": "2003-12-06",
                "email": "usuario@email.com",
                "phoneNumber": "6821879908",
                "mobileNumber": "11913075437"
            }
            """;

    @Test
    @DisplayName("Should return 500 when an unexpected exception occurs")
    void shouldReturn500ForGenericException() throws Exception {

        when(service.findByUuid(uuid))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/people/{uuid}", uuid))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("Should return 404 when resource is not found")
    void shouldReturn404ForResourceNotFound() throws Exception {
        when(service.findByUuid(uuid))
                .thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get("/api/v1/people/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Should return 422 when a business rule is violated")
    void shouldReturn422ForBusinessRule() throws Exception {
        when(service.findByUuid(uuid))
                .thenThrow(new BusinessRulesException("Regra de negócio inválida"));

        mockMvc.perform(get("/api/v1/people/{uuid}", uuid))
                .andExpect(status().isUnprocessableContent());
    }


    @Test
    @DisplayName("Should return 400 when request body contains malformed JSON")
    void shouldReturn400ForMalformedJson() throws Exception {
        String malformedJson = """
                {
                    "firstName": "Carla",
                    "email":
                }
                """;
        mockMvc.perform(post("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Should return 422 when request body validation fails")
    void shouldReturn422ForMethodArgumentNotValid() throws Exception {
        String invalidPersonJson = """
                {
                    "firstName": "",
                    "surname": "Batista Silva",
                    "document": "65824217866",
                    "birthDate": "2003-12-06",
                    "email": "usuario@email.com",
                    "phoneNumber": "6821879908",
                    "mobileNumber": "11913075437"
                }
                """;

        mockMvc.perform(post("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidPersonJson))
                .andExpect(status().isUnprocessableContent());
    }


    @Test
    @DisplayName("Should return 409 when a data integrity violation occurs")
    void shouldReturn409ForDataIntegrityViolation() throws Exception {

        when(service.insert(any(PersonDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Erro de integridade de dados"));

        mockMvc.perform(post("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_PERSON_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return 409 when email unique constraint is violated")
    void shouldReturn409ForDuplicatedEmail() throws Exception {
        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Integrity violation", new RuntimeException("Violation of constraint UK_PERSON_EMAIL"));

        when(service.insert(any(PersonDTO.class)))
                .thenThrow(exception);

        mockMvc.perform(post("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_PERSON_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Este e-mail já está cadastrado"));
    }

    @Test
    @DisplayName("Should return 409 when document unique constraint is violated")
    void shouldReturn409ForDuplicatedDocument() throws Exception {

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Integrity violation", new RuntimeException("Violation of constraint UK_PERSON_DOCUMENT"));

        when(service.insert(any(PersonDTO.class)))
                .thenThrow(exception);

        mockMvc.perform(post("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_PERSON_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Este documento já está cadastrado"));
    }
}
