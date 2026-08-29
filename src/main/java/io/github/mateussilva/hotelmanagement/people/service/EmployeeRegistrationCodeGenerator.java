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

        Optional<EmployeeRegistrationSequence> existingSequence = repository.findForUpdate(hotelId, year);

        long number;

        if (existingSequence.isPresent())
            number = existingSequence.get().next();
        else {
            EmployeeRegistrationSequence sequence = EmployeeRegistrationSequence.of(hotelId, year);
            number = sequence.next();
            repository.save(sequence);
        }

        return "%02d-%d-%04d".formatted(hotelId, year, number);
    }
}
