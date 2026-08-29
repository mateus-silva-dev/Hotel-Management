package io.github.mateussilva.hotelmanagement.people.service;

import io.github.mateussilva.hotelmanagement.hotel.HotelLookup;
import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeCreateDTO;
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
    public EmployeeDetailsProjection findDetailsByUuid(UUID uuid) {
        return repository.findDetailsByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeMinProjection> findAllMin(EmployeeFilterDTO filter, Pageable pageable) {
        return repository.findAllMinWithFilters(
                filter.firstName(), filter.surname(), filter.registrationCode(), filter.status(), pageable);
    }

    @Transactional
    public Employee create(EmployeeCreateDTO dto) {
        Person person = personService.findOrCreate(dto.person());
        JobPosition job = jobPositionService.findDetailsByUuid(dto.jobPosition());
        Hotel hotel = hotelLookup.findEntityByUuid(dto.hotel());

        String registrationCode = registrationCodeGenerator.generate(hotel, dto.hireDate());
        return repository.save(mapper.toEntity(person, hotel, job, registrationCode, dto));
    }

    @Transactional
    public void updateJobPosition(UUID uuid, UUID uuidNewJobPosition) {
        Employee employee = findEntityByUuidOrThrow(uuid);
        JobPosition job = jobPositionService.findDetailsByUuid(uuidNewJobPosition);
        employee.changeJobPosition(job);
    }

    @Transactional
    public void activate(UUID uuid) {
        findEntityByUuidOrThrow(uuid).activate();
    }

    @Transactional
    public void putOnLeave(UUID uuid) {
        findEntityByUuidOrThrow(uuid).putOnLeave();
    }

    @Transactional
    public void terminate(UUID uuid, LocalDate dismissalDate) {
        findEntityByUuidOrThrow(uuid).terminate(dismissalDate);
    }

    private Employee findEntityByUuidOrThrow(UUID uuid) {
        return repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

}
