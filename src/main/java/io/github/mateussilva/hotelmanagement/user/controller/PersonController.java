package io.github.mateussilva.hotelmanagement.user.controller;

import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonDTO;
import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonFilterDTO;
import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonUpdateDTO;
import io.github.mateussilva.hotelmanagement.user.domain.Person;
import io.github.mateussilva.hotelmanagement.user.mapper.PersonMapper;
import io.github.mateussilva.hotelmanagement.user.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/people")
public class PersonController {

    private final PersonService service;
    private final PersonMapper mapper;

    public PersonController(PersonService service, PersonMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PersonDTO> findByUuid(@PathVariable UUID uuid) {
        Person person = service.findByUuid(uuid);
        return ResponseEntity.ok(mapper.toDTO(person));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            PersonFilterDTO filter,
            Pageable pageable,
            PagedResourcesAssembler<PersonDTO> assembler) {

        Page<Person> people = service.findAll(filter, pageable);
        Page<PersonDTO> page = people.map(mapper::toDTO);
        PagedModel<EntityModel<PersonDTO>> model = assembler.toModel(page);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> insert(@Valid @RequestBody PersonDTO dto) {
        Person person = service.insert(dto);
        PersonDTO personDTO = mapper.toDTO(person);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(person.getUuid())
                .toUri();
        return ResponseEntity.created(uri).body(personDTO);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<PersonDTO> update(@PathVariable UUID uuid, @Valid @RequestBody PersonUpdateDTO dto) {
        Person person = service.update(uuid, dto);
        return ResponseEntity.ok(mapper.toDTO(person));
    }
}
