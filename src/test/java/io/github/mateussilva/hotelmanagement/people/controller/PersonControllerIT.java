package io.github.mateussilva.hotelmanagement.people.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Integration Tests - Person Controller Real Flow")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/people";
    }

    @Nested
    @DisplayName("Find person by UUID")
    class FindPersonByUuid {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("Should return a 200 - person by UUID")
            void validUuid() {
                UUID uuidValid = UUID.fromString("1ace1347-2f4a-4db9-88e7-36d2bac76a8c");

                given()
                            .contentType("application/json")
                            .pathParam("uuid", uuidValid)
                    .when()
                            .get("/{uuid}")
                    .then()
                            .statusCode(200)
                            .body("uuid", equalTo(uuidValid.toString()))
                            .body("firstName", equalTo("Maria"))
                            .body("surname", equalTo("Ferreira Pereira"))
                            .body("document", equalTo("42957390060"))
                            .body("birthDate", equalTo("1962-07-19"))
                            .body("email", equalTo("maria@email.com"))
                            .body("phoneNumber", equalTo("6821879908"))
                            .body("mobileNumber", equalTo("11913075437"));
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("Should return a 404 - the person is not found")
            void invalidUuid() {
                UUID nonExistentUuid = UUID.randomUUID();

                given()
                            .contentType("application/json")
                            .pathParam("uuid", nonExistentUuid)
                    .when()
                            .get("/{uuid}")
                    .then()
                            .statusCode(404);
            }

            @Test
            @DisplayName("Should return a 400 - When UUID is malformed")
            void invalidUuidIsMalformed() {
                String malformedUuid = "invalid-uuid";

                given()
                            .contentType("application/json")
                            .pathParam("uuid", malformedUuid)
                        .when()
                            .get("/{uuid}")
                        .then()
                            .statusCode(400);
            }

        }
    }

    @Nested
    @DisplayName("Find all People")
    class FindAllPeople {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("Should return a 200 - Page of People")
            void valid() {
                given()
                        .contentType("application/json")
                    .when()
                        .get()
                    .then()
                        .statusCode(200);
            }

            @Test
            @DisplayName("Should return a 200 - And the person corresponding to the first name")
            void searchPersonByFirstName() {
                given()
                        .contentType("application/json")
                        .param("firstName", "maria")
                    .when()
                            .get()
                    .then()
                            .statusCode(200)
                            .body("content[0].firstName", equalTo("Maria"))
                            .body("content[0].surname", equalTo("Ferreira Pereira"))
                            .body("content[0].email", equalTo("maria@email.com"));
            }

            @Test
            @DisplayName("Should return a 200 - And the person corresponding to the surname")
            void searchPersonBySurname() {
                given()
                            .contentType("application/json")
                            .param("surname", "Ferreira")
                    .when()
                            .get()
                    .then()
                            .statusCode(200)
                            .body("content[0].firstName", equalTo("Maria"))
                            .body("content[0].surname", equalTo("Ferreira Pereira"))
                            .body("content[0].email", equalTo("maria@email.com"));
            }

            @Test
            @DisplayName("Should return a 200 - And the person corresponding to the document")
            void searchPersonByDocument() {
                given()
                            .contentType("application/json")
                            .param("document", "18257746843")
                    .when()
                            .get()
                    .then()
                            .statusCode(200)
                            .body("content[0].firstName", equalTo("Otávio"))
                            .body("content[0].surname", equalTo("César Ian Martins"))
                            .body("content[0].email", equalTo("otavio@email.com"));
            }

            @Test
            @DisplayName("Should return a 200 - And the person corresponding to the email")
            void searchPersonByEmail() {
                given()
                            .contentType("application/json")
                            .param("email", "otavio@email.com")
                    .when()
                            .get()
                    .then()
                            .statusCode(200)
                            .body("content[0].firstName", equalTo("Otávio"))
                            .body("content[0].surname", equalTo("César Ian Martins"))
                            .body("content[0].email", equalTo("otavio@email.com"));
            }

        }

    }

    @Nested
    @DisplayName("Insert new Person")
    class InsertNewPerson {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("Should return a 201 - And save the data to the database")
            void validInsert() {
                String newPerson = """
                        {
                            "firstName" : "Talita",
                            "surname" : "Xavier Jr.",
                            "document" : "027.651.338-08",
                            "birthDate" : "2004-12-04",
                            "email" : "talita.jr@4devtools.com",
                            "phoneNumber" : "6824794506",
                            "mobileNumber" : "68994276487"
                        }
                """;

                given()
                            .contentType("application/json")
                            .body(newPerson)
                        .when()
                            .post()
                        .then()
                            .statusCode(201);
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("Should return a 400 - When JSON format is malformed")
            void invalidInsertJsonIsMalformed() {
                String jsonMalformed = "{ \"firstName\": \"Ana\" ";

                given()
                            .contentType("application/json")
                            .body(jsonMalformed)
                        .when()
                            .post()
                        .then()
                            .statusCode(400);
            }

            @Test
            @DisplayName("Should return a 409 - When document or email already exists")
            void invalidInsertDocumentOrEmailExists() {
                String jsonDuplicated  = """
                        {
                            "firstName" : "Talita",
                            "surname" : "Xavier Jr.",
                            "document" : "429.573.900-60",
                            "birthDate" : "2004-12-04",
                            "email" : "maria@email.com",
                            "phoneNumber" : "6824794506",
                            "mobileNumber" : "68994276487"
                        }
                """;
                given()
                            .contentType("application/json")
                            .body(jsonDuplicated)
                        .when()
                            .post()
                        .then()
                            .statusCode(409);
            }

            @Test
            @DisplayName("Should return a 422 - When data is invalid")
            void invalidInsertDataIsInvalid() {
                String dataInvalid  = """
                        {
                            "firstName" : "Talita",
                            "surname" : " Xavier Jr.",
                            "document" : "429.573.900",
                            "birthDate" : "2004-12-04",
                            "email" : "mariaemail.com",
                            "phoneNumber" : "6824794506",
                            "mobileNumber" : "6899427-6487"
                        }
                """;
                given()
                            .contentType("application/json")
                            .body(dataInvalid)
                        .when()
                            .post()
                        .then()
                            .statusCode(422);
            }

        }

    }

    @Nested
    @DisplayName("Update Person")
    class UpdatePerson {

        @Nested
        @DisplayName("Scenario for success")
        class Success {

            @Test
            @DisplayName("Should return a 200 - And update the data")
            void validUpdate() {
                String newPerson = """
                        {
                            "firstName" : "Fabiana",
                            "surname" : "Tereza Clara da Cruz",
                            "document" : "384.978.628-56",
                            "birthDate" : "1957-08-02",
                            "email" : "fabiana.tereza.dacruz@aguabr.com.br",
                            "phoneNumber" : "6228105052",
                            "mobileNumber" : "62985160704"
                        }
                """;

                String uuidGerado = given()
                            .contentType("application/json")
                            .body(newPerson)
                        .when()
                            .post()
                        .then()
                            .statusCode(201)
                            .extract().path("uuid");

                String updatePerson = """
                        {
                            "newEmail" : "novo-email@email.com",
                            "newPhoneNumber" : "1144448888",
                            "newMobileNumber" : "11922225555"
                        }
                """;

                given()
                            .contentType("application/json")
                            .pathParam("uuid", uuidGerado)
                            .body(updatePerson)
                        .when()
                            .patch("/{uuid}")
                        .then()
                            .statusCode(200);
            }

        }

        @Nested
        @DisplayName("Failure scenario")
        class Failure {

            @Test
            @DisplayName("Should return a 400 - When JSON format is malformed")
            void invalidUpdateJsonIsMalformed() {
                String jsonMalformed = "{ \"newEmail\": \"Ana\" ";

                given()
                            .contentType("application/json")
                            .body(jsonMalformed)
                        .when()
                            .post()
                        .then()
                            .statusCode(400);
            }

            @Test
            @DisplayName("Should return a 404 - the person is not found")
            void invalidUuid() {
                UUID nonExistentOrInvalidUUID = UUID.randomUUID();

                given()
                            .contentType("application/json")
                            .pathParam("uuid", nonExistentOrInvalidUUID)
                        .when()
                            .get("/{uuid}")
                        .then()
                            .statusCode(404);
            }

            @Test
            @DisplayName("Should return a 409 - When email already exists")
            void invalidUpdateEmailExists() {
                UUID uuidValid = UUID.fromString("1ace1347-2f4a-4db9-88e7-36d2bac76a8c");
                String updatePerson = """
                        {
                            "newEmail" : "otavio@email.com"
                        }
                """;

                given()
                            .contentType("application/json")
                            .pathParam("uuid", uuidValid)
                            .body(updatePerson)
                        .when()
                            .patch("/{uuid}")
                        .then()
                            .log().ifValidationFails()
                            .statusCode(409);
            }

            @Test
            @DisplayName("Should return a 422 - When email is malformed")
            void invalidUpdateEmailMalformed() {
                UUID uuidValid = UUID.fromString("1ace1347-2f4a-4db9-88e7-36d2bac76a8c");
                String updatePerson = """
                        {
                            "newEmail" : "novo-emailemail.com"
                        }
                """;

                given()
                            .contentType("application/json")
                            .pathParam("uuid", uuidValid)
                            .body(updatePerson)
                        .when()
                            .patch("/{uuid}")
                        .then()
                            .log().ifValidationFails()
                            .statusCode(422);
            }

            @Test
            @DisplayName("Should return a 422 - When phone number is malformed")
            void invalidUpdatePhoneNumberMalformed() {
                UUID uuidValid = UUID.fromString("1ace1347-2f4a-4db9-88e7-36d2bac76a8c");
                String updatePerson = """
                        {
                            "newPhoneNumber" : "114444"
                        }
                """;

                given()
                            .contentType("application/json")
                            .pathParam("uuid", uuidValid)
                            .body(updatePerson)
                        .when()
                            .patch("/{uuid}")
                        .then()
                            .log().ifValidationFails()
                            .statusCode(422);
            }

            @Test
            @DisplayName("Should return a 422 - When mobile number is malformed")
            void invalidUpdateMobileNumberMalformed() {
                UUID uuidValid = UUID.fromString("1ace1347-2f4a-4db9-88e7-36d2bac76a8c");
                String updatePerson = """
                        {
                            "newMobileNumber" : "1192222"
                        }
                """;

                given()
                            .contentType("application/json")
                            .pathParam("uuid", uuidValid)
                            .body(updatePerson)
                        .when()
                            .patch("/{uuid}")
                        .then()
                            .log().ifValidationFails()
                            .statusCode(422);
            }

        }

    }

}