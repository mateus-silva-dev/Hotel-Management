package io.github.mateussilva.hotelmanagement.people.controller;

import io.github.mateussilva.hotelmanagement.people.controller.assembler.EmployeeDetailsModelAssembler;
import io.github.mateussilva.hotelmanagement.people.controller.assembler.EmployeeMinModelAssembler;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.*;
import io.github.mateussilva.hotelmanagement.people.mapper.EmployeeMapper;
import io.github.mateussilva.hotelmanagement.people.projections.EmployeeMinProjection;
import io.github.mateussilva.hotelmanagement.people.service.EmployeeService;
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
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final EmployeeMapper mapper;
    private final EmployeeMinModelAssembler modelAssembler;
    private final EmployeeDetailsModelAssembler modelDetailsAssembler;

    public EmployeeController(EmployeeService service, EmployeeMapper mapper, EmployeeMinModelAssembler modelAssembler, EmployeeDetailsModelAssembler modelDetailsAssembler) {
        this.service = service;
        this.mapper = mapper;
        this.modelAssembler = modelAssembler;
        this.modelDetailsAssembler = modelDetailsAssembler;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EntityModel<EmployeeDetailsDTO>> findByUuid(
            @PathVariable UUID uuid
    ) {
        EmployeeDetailsDTO employeeDTO = mapper.toDetailsDTO(service.findDetailsByUuid(uuid));
        EntityModel<EmployeeDetailsDTO> model = modelDetailsAssembler.toModel(employeeDTO);
        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<EmployeeMinDTO>>> findAll(
            EmployeeFilterDTO filter,
            Pageable pageable,
            PagedResourcesAssembler<EmployeeMinDTO> assembler) {

        Page<EmployeeMinProjection> employees = service.findAllMin(filter, pageable);
        Page<EmployeeMinDTO> page = employees.map(mapper::toMinDTO);
        PagedModel<EntityModel<EmployeeMinDTO>> model = assembler.toModel(page, modelAssembler);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<EmployeeDetailsDTO> insert(
            @Valid @RequestBody EmployeeCreateDTO dto
    ) {
        EmployeeDetailsDTO employeeDTO = mapper.toDetailsDTO(service.create(dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(employeeDTO.uuid())
                .toUri();
        return ResponseEntity.created(uri).body(employeeDTO);
    }

    @PatchMapping("/{uuid}/job-position")
    public ResponseEntity<Void> updateJobPosition(@PathVariable UUID uuid, @RequestBody UUID jobPositionUuid) {
        service.updateJobPosition(uuid, jobPositionUuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID uuid) {
        service.activate(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/putOnLeave")
    public ResponseEntity<Void> putOnLeave(@PathVariable UUID uuid) {
        service.putOnLeave(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{uuid}/terminate")
    public ResponseEntity<Void> terminate(@PathVariable UUID uuid, @RequestParam LocalDate dismissalDate) {
        service.terminate(uuid, dismissalDate);
        return ResponseEntity.noContent().build();
    }
}
