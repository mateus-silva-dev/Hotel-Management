package io.github.mateussilva.hotelmanagement.people.service;

import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.people.domain.EmployeeRegistrationSequence;
import io.github.mateussilva.hotelmanagement.people.repository.EmployeeRegistrationSequenceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class EmployeeRegistrationCodeGenerator {

    private final EmployeeRegistrationSequenceRepository repository;

    public EmployeeRegistrationCodeGenerator(EmployeeRegistrationSequenceRepository repository) {
        this.repository = repository;
    }

    public String generate(Hotel hotel, LocalDate hireDate) {
        Long hotelId = hotel.getId();
        int year = hireDate.getYear();

        EmployeeRegistrationSequence sequence =
                repository.findForUpdate(hotelId, year)
                        .orElseGet(() -> repository.save(
                                EmployeeRegistrationSequence.of(hotelId, year)));

        long number = sequence.next();

        return "%02d-%d-%04d".formatted(hotelId, year, number);
    }
}
