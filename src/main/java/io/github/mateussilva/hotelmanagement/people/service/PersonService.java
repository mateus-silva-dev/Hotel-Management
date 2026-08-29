package io.github.mateussilva.hotelmanagement.people.service;

import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonCreateDTO;
import io.github.mateussilva.hotelmanagement.people.domain.CPF;
import io.github.mateussilva.hotelmanagement.people.projections.PersonDetailsProjection;
import io.github.mateussilva.hotelmanagement.people.projections.PersonMinProjection;
import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonFilterDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonUpdateDTO;
import io.github.mateussilva.hotelmanagement.people.domain.Person;
import io.github.mateussilva.hotelmanagement.people.mapper.PersonMapper;
import io.github.mateussilva.hotelmanagement.people.repository.PersonRepository;
import io.github.mateussilva.hotelmanagement.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository repository;
    private final PersonMapper mapper;

    public PersonService(PersonRepository repository, PersonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public PersonDetailsProjection findDetailsByUuid(UUID uuid) {
        return repository.findDetailsByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<PersonMinProjection> findAllMin(PersonFilterDTO filter, Pageable pageable) {
        return repository.findAllMinWithFilters(
                filter.firstName(), filter.surname(), filter.email(), filter.document(), pageable);
    }

    @Transactional
    public Person create(PersonCreateDTO dto) {
        return repository.save(mapper.toEntity(dto));
    }

    @Transactional
    public Person update(UUID uuid, PersonUpdateDTO dto) {
        Person person = repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);

        Optional.ofNullable(dto.newEmail())
                .map(Email::new)
                .ifPresent(person::updateEmail);

        Optional.ofNullable(dto.newPhoneNumber())
                .ifPresent(person::updatePhoneNumber);

        Optional.ofNullable(dto.newMobileNumber())
                .ifPresent(person::updateMobileNumber);

        return repository.save(person);
    }

    @Transactional
    public Person findOrCreate(PersonCreateDTO dto) {
        CPF cpf = new CPF(dto.document());

        return repository.findByDocument(cpf)
                .orElseGet(() -> repository.save(mapper.toEntity(dto)));
    }

}