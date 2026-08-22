package io.github.mateussilva.hotelmanagement.user.service;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonDTO;
import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonFilterDTO;
import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonUpdateDTO;
import io.github.mateussilva.hotelmanagement.user.domain.Person;
import io.github.mateussilva.hotelmanagement.user.mapper.PersonMapper;
import io.github.mateussilva.hotelmanagement.user.repository.PersonRepository;
import io.github.mateussilva.hotelmanagement.shared.exception.ResourceNotFoundException;
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
        Person person = mapper.toEntity(dto);
        repository.save(person);
        return person;
    }

    @Transactional
    public Person update(UUID uuid, PersonUpdateDTO dto) {
        Person person = repository.findByUuid(uuid).orElseThrow(ResourceNotFoundException::new);

        if (dto.newEmail() != null) {
            Email email = new Email(dto.newEmail());
            person.updateEmail(email);
        }
        if (dto.newPhoneNumber() != null)
            person.updatePhoneNumber(dto.newPhoneNumber());
        if (dto.newMobileNumber() != null)
            person.updateMobileNumber(dto.newMobileNumber());

        return repository.save(person);
    }

}
