package io.github.mateussilva.hotelmanagement.people.domain;

import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.shared.doc.Generated;
import io.github.mateussilva.hotelmanagement.people.domain.enums.StatusEmployee;
import io.github.mateussilva.hotelmanagement.people.domain.exception.InvalidEmployeeException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_employee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Employee {

    private static final String REGISTRATION_CODE_REQUIRED_MESSAGE = "Um código de registro deve ser informado";
    private static final String HIRE_DATE_REQUIRED_MESSAGE = "A data de contratação deve ser informada";
    private static final String PERSON_REQUIRED_MESSAGE = "Uma pessoa deve ser informada";
    private static final String HOTEL_REQUIRED_MESSAGE = "O hotel deve ser informado";
    private static final String JOB_POSITION_REQUIRED_MESSAGE = "O cargo deve ser informado";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private String registrationCode;

    private LocalDate hireDate;

    private LocalDate dismissalDate;

    @Enumerated(EnumType.STRING)
    private StatusEmployee status = StatusEmployee.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_position_id", nullable = false)
    private JobPosition jobPosition;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;


    @Generated
    private Employee(Person person, Hotel hotel, JobPosition jobPosition, String registrationCode, LocalDate hireDate) {
        validateCreation(person, hotel, jobPosition, registrationCode, hireDate);
        this.uuid = UUID.randomUUID();
        this.person = person;
        this.hotel = hotel;
        this.jobPosition = jobPosition;
        this.registrationCode = registrationCode;
        this.hireDate = hireDate;
    }

    public static Employee of(Person person, Hotel hotel, JobPosition jobPosition, String registrationCode, LocalDate hireDate) {
        return new Employee(person, hotel, jobPosition, registrationCode, hireDate);
    }


    public void activate() {
        this.status = this.status.transitionTo(StatusEmployee.ACTIVE);
    }

    public void putOnLeave() {
        this.status = this.status.transitionTo(StatusEmployee.ON_LEAVE);
    }

    public void terminate(LocalDate dismissalDate) {
        this.status = this.status.transitionTo(StatusEmployee.TERMINATED);
        this.dismissalDate = dismissalDate;
    }

    public void changeJobPosition(JobPosition newJobPosition) {
        requireNonNull(jobPosition, JOB_POSITION_REQUIRED_MESSAGE);
        if (Objects.equals(newJobPosition, this.jobPosition)) return;
        this.jobPosition = newJobPosition;
    }


    private static void validateCreation(Person person, Hotel hotel, JobPosition jobPosition, String registrationCode, LocalDate hireDate) {
        requireNonNull(person, PERSON_REQUIRED_MESSAGE);
        requireNonNull(hotel, HOTEL_REQUIRED_MESSAGE);
        requireNonNull(jobPosition, JOB_POSITION_REQUIRED_MESSAGE);
        requireText(registrationCode, REGISTRATION_CODE_REQUIRED_MESSAGE);
        requireNonNull(hireDate, HIRE_DATE_REQUIRED_MESSAGE);
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
