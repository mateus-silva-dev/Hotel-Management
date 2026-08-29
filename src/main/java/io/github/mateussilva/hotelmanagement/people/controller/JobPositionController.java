package io.github.mateussilva.hotelmanagement.people.controller;

import io.github.mateussilva.hotelmanagement.people.controller.dto.jobposition.JobPositionDTO;
import io.github.mateussilva.hotelmanagement.people.domain.JobPosition;
import io.github.mateussilva.hotelmanagement.people.mapper.JobPositionMapper;
import io.github.mateussilva.hotelmanagement.people.service.JobPositionService;
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
@RequestMapping("api/v1/jobpositions")
public class JobPositionController {

    private final JobPositionService service;
    private final JobPositionMapper mapper;

    public JobPositionController(JobPositionService service, JobPositionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<JobPositionDTO> findByUuid(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(mapper.toDTO(service.findDetailsByUuid(uuid)));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<JobPositionDTO>>> findAll(
            Pageable pageable,
            PagedResourcesAssembler<JobPositionDTO> assembler
    ) {
        Page<JobPositionDTO> page = service.findAllDetails(pageable).map(mapper::toDTO);
        return ResponseEntity.ok(assembler.toModel(page));
    }

    @PostMapping
    public ResponseEntity<JobPositionDTO> insert(
            @Valid @RequestBody JobPositionDTO dto
    ) {
        JobPositionDTO jobDTO = mapper.toDTO(service.create(dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(dto.uuid())
                .toUri();
        return ResponseEntity.created(uri).body(jobDTO);
    }

    @PatchMapping("/{uuid}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID uuid) {
        service.activate(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID uuid) {
        service.deactivate(uuid);
        return ResponseEntity.noContent().build();
    }

}
