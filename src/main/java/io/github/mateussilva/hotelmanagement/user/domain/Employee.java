package io.github.mateussilva.hotelmanagement.user.domain;

import io.github.mateussilva.hotelmanagement.shared.doc.Generated;
import io.github.mateussilva.hotelmanagement.user.domain.enums.StatusEmployee;
import io.github.mateussilva.hotelmanagement.shared.exception.BusinessRulesException;
import io.github.mateussilva.hotelmanagement.user.domain.exception.InvalidEmployeeException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_employee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Employee {

    private static final String REGISTRATION_CODE_REQUIRED_MESSAGE = "Um código de registro deve ser informado";
    private static final String HIRE_DATE_REQUIRED_MESSAGE = "A data de contratação deve ser informada";
    private static final String JOB_TITLE_REQUIRED_MESSAGE = "O cargo deve ser informado";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private String registrationCode;

    private LocalDate hireDate;

    private LocalDate dismissalDate;

    @Enumerated(EnumType.STRING)
    private StatusEmployee status = StatusEmployee.ACTIVE;

    private String jobTitle;


    @Generated
    private Employee(String registrationCode, LocalDate hireDate, String jobTitle) {
        validateCreation(registrationCode, hireDate, jobTitle);
        this.uuid = UUID.randomUUID();
        this.registrationCode = registrationCode;
        this.hireDate = hireDate;
        this.jobTitle = jobTitle.toUpperCase();
    }

    public static Employee of(String registrationCode, LocalDate hireDate, String jobTitle) {
        return new Employee(registrationCode, hireDate, jobTitle);
    }


    private static void validateCreation(String registrationCode, LocalDate hireDate, String jobTitle) {
        requireText(registrationCode, REGISTRATION_CODE_REQUIRED_MESSAGE);
        requireNonNull(hireDate, HIRE_DATE_REQUIRED_MESSAGE);
        requireText(jobTitle, JOB_TITLE_REQUIRED_MESSAGE);
    }

    private static void requireText(String value, String message) {
        if (isBlank(value))
            throw new InvalidEmployeeException(message);
    }

    private static void requireNonNull(Object value, String message) {
        if (value == null)
            throw new InvalidEmployeeException(message);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }


    @Generated
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Employee employee)) return false;

        return Objects.equals(getUuid(), employee.getUuid());
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }
}
