package io.github.mateussilva.hotelmanagement.user.service;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonDTO;
import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonFilterDTO;
import io.github.mateussilva.hotelmanagement.user.domain.CPF;
import io.github.mateussilva.hotelmanagement.user.domain.Person;
import io.github.mateussilva.hotelmanagement.user.mapper.PersonMapper;
import io.github.mateussilva.hotelmanagement.user.repository.PersonRepository;
import io.github.mateussilva.hotelmanagement.user.service.exception.DocumentAlreadyRegisteredException;
import io.github.mateussilva.hotelmanagement.user.service.exception.EmailAlreadyRegisteredException;
import io.github.mateussilva.hotelmanagement.user.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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
    public Person findByUuid(UUID uuid) {
        return repository.findByUuid(uuid).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<Person> findAll(PersonFilterDTO filter, Pageable pageable) {
        return repository.searchWithFilters(
                        filter.firstName(), filter.surname(), filter.document(), filter.email(), pageable);
    }

    @Transactional
    public Person insert(PersonDTO dto) {
        checkDataExistsInCreation(new CPF(dto.document()), new Email(dto.email()));
        Person person = mapper.toEntity(dto);
        return repository.save(person);
    }


    private void checkEmailExists(Email email) {
        if (repository.existsByEmail(email))
            throw new EmailAlreadyRegisteredException();
    }

    private void checkDataExistsInCreation(CPF cpf, Email email) {
        if (repository.existsByDocument(cpf))
            throw new DocumentAlreadyRegisteredException();

        if (repository.existsByEmail(email))
            throw new EmailAlreadyRegisteredException();
    }
}
