package io.github.mateussilva.hotelmanagement.people.controller;

import io.github.mateussilva.hotelmanagement.people.controller.assembler.PersonMinModelAssembler;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.*;
import io.github.mateussilva.hotelmanagement.people.mapper.PersonMapper;
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
    public ResponseEntity<PersonDetailsDTO> findDetailsByUuid(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(mapper.toDetailsDTO(service.findDetailsByUuid(uuid)));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PersonMinDTO>>> findAllMin(
            PersonFilterDTO filter,
            Pageable pageable,
            PagedResourcesAssembler<PersonMinDTO> assembler
    ) {
        Page<PersonMinProjection> people = service.findAllMin(filter, pageable);
        Page<PersonMinDTO> page = people.map(mapper::toMinDTO);
        PagedModel<EntityModel<PersonMinDTO>> model = assembler.toModel(page, modelAssembler);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<PersonDetailsDTO> create(
            @RequestBody @Valid PersonCreateDTO dto
    ) {
        PersonDetailsDTO personDTO = mapper.toDetailsDTO(service.create(dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(personDTO.uuid())
                .toUri();
        return ResponseEntity.created(uri).body(personDTO);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<PersonDetailsDTO> update(
            @PathVariable UUID uuid,
            @RequestBody @Valid PersonUpdateDTO dto
    ) {
        return ResponseEntity.ok(mapper.toDetailsDTO(service.update(uuid, dto)));
    }
}
