package io.github.mateussilva.hotelmanagement.people.domain;

import io.github.mateussilva.hotelmanagement.shared.doc.Generated;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_employee_registration_sequence",
        uniqueConstraints = @UniqueConstraint(columnNames = {"hotel_id", "year"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeRegistrationSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hotelId;

    private Integer registrationYear;

    private Long nextValue;


    @Generated
    private EmployeeRegistrationSequence(Long hotelId, Integer registrationYear) {
        this.hotelId = hotelId;
        this.registrationYear = registrationYear;
        this.nextValue = 1L;
    }

    @Generated
    public static EmployeeRegistrationSequence of(Long hotelId, Integer year) {
        return new EmployeeRegistrationSequence(hotelId, year);
    }


    public long next() {
        long current = this.nextValue;
        this.nextValue++;
        return current;
    }

}
