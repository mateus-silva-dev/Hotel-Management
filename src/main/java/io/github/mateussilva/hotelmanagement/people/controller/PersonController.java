package io.github.mateussilva.hotelmanagement.people.controller;

import io.github.mateussilva.hotelmanagement.people.controller.assembler.PersonMinModelAssembler;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.*;
import io.github.mateussilva.hotelmanagement.people.domain.Person;
import io.github.mateussilva.hotelmanagement.people.mapper.PersonMapper;
import io.github.mateussilva.hotelmanagement.people.projections.PersonDetailsProjection;
import io.github.mateussilva.hotelmanagement.people.projections.PersonMinProjection;
import io.github.mateussilva.hotelmanagement.people.service.PersonService;
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
import java.util.UUID;

@RestController
@RequestMapping("api/v1/people")
public class PersonController {

    private final PersonService service;
    private final PersonMapper mapper;
    private final PersonMinModelAssembler modelAssembler;

    public PersonController(PersonService service, PersonMapper mapper, PersonMinModelAssembler modelAssembler) {
        this.service = service;
        this.mapper = mapper;
        this.modelAssembler = modelAssembler;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PersonDetailsDTO> findByUuid(@PathVariable UUID uuid) {
        PersonDetailsProjection person = service.findByUuid(uuid);
        return ResponseEntity.ok(mapper.toDetailsDTO(person));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PersonMinDTO>>> findAll(
            PersonFilterDTO filter,
            Pageable pageable,
            PagedResourcesAssembler<PersonMinDTO> assembler) {

        Page<PersonMinProjection> people = service.findAll(filter, pageable);
        Page<PersonMinDTO> page = people.map(mapper::toMinDTO);
        PagedModel<EntityModel<PersonMinDTO>> model = assembler.toModel(page, modelAssembler);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> insert(@RequestBody @Valid PersonDTO dto) {
        Person person = service.insert(dto);
        PersonDTO personDTO = mapper.toDTO(person);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(person.getUuid())
                .toUri();
        return ResponseEntity.created(uri).body(personDTO);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<PersonDTO> update(@PathVariable UUID uuid, @RequestBody @Valid PersonUpdateDTO dto) {
        Person person = service.update(uuid, dto);
        return ResponseEntity.ok(mapper.toDTO(person));
    }
}
