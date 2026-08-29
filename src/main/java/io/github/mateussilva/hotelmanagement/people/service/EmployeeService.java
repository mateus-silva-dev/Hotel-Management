package io.github.mateussilva.hotelmanagement.people.service;

import io.github.mateussilva.hotelmanagement.hotel.HotelLookup;
import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.hotel.service.HotelService;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeFilterDTO;
import io.github.mateussilva.hotelmanagement.people.domain.Employee;
import io.github.mateussilva.hotelmanagement.people.domain.JobPosition;
import io.github.mateussilva.hotelmanagement.people.domain.Person;
import io.github.mateussilva.hotelmanagement.people.mapper.EmployeeMapper;
import io.github.mateussilva.hotelmanagement.people.projections.EmployeeDetailsProjection;
import io.github.mateussilva.hotelmanagement.people.projections.EmployeeMinProjection;
import io.github.mateussilva.hotelmanagement.people.repository.EmployeeRepository;
import io.github.mateussilva.hotelmanagement.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final PersonService personService;
    private final JobPositionService jobPositionService;
    private final EmployeeMapper mapper;
    private final HotelLookup hotelLookup;
    private final EmployeeRegistrationCodeGenerator registrationCodeGenerator;

    public EmployeeService(EmployeeRepository repository, PersonService personService, JobPositionService jobPositionService, EmployeeMapper mapper, HotelLookup hotelLookup, EmployeeRegistrationCodeGenerator registrationCodeGenerator) {
        this.repository = repository;
        this.personService = personService;
        this.jobPositionService = jobPositionService;
        this.mapper = mapper;
        this.hotelLookup = hotelLookup;
        this.registrationCodeGenerator = registrationCodeGenerator;
    }

    @Transactional(readOnly = true)
    public EmployeeDetailsProjection findByUuid(UUID uuid) {
        return repository.findDetailsByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeMinProjection> findAll(EmployeeFilterDTO filter, Pageable pageable) {
        return repository.findAllMinWithFilters(
                filter.firstName(), filter.surname(), filter.registrationCode(), filter.status(), pageable);
    }

    @Transactional
    public Employee insert(EmployeeDTO dto) {
        Person person = personService.findOrCreate(dto.person());
        JobPosition job = jobPositionService.findByUuid(dto.jobPosition());
        Hotel hotel = hotelLookup.findEntityByUuid(dto.hotel());

        String registrationCode = registrationCodeGenerator.generate(hotel, dto.hireDate());
        Employee employee = mapper.toEntity(person, hotel, job, registrationCode, dto);
        repository.save(employee);
        return employee;
    }

    @Transactional
    public void updateJobPosition(UUID uuid, UUID uuidNewJobPosition) {
        Employee employee = findEntityByUuidOrThrow(uuid);
        JobPosition job = jobPositionService.findByUuid(uuidNewJobPosition);
        employee.changeJobPosition(job);
    }

    @Transactional
    public void activate(UUID uuid) {
        Employee employee = findEntityByUuidOrThrow(uuid);
        employee.activate();
    }

    @Transactional
    public void putOnLeave(UUID uuid) {
        Employee employee = findEntityByUuidOrThrow(uuid);
        employee.putOnLeave();
    }

    @Transactional
    public void terminate(UUID uuid, LocalDate dismissalDate) {
        Employee employee = findEntityByUuidOrThrow(uuid);
        employee.terminate(dismissalDate);
    }

    private Employee findEntityByUuidOrThrow(UUID uuid) {
        return repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

}
