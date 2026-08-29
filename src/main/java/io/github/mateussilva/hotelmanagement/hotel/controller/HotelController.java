package io.github.mateussilva.hotelmanagement.hotel.controller;

import io.github.mateussilva.hotelmanagement.hotel.controller.assembler.HotelMinModelAssembler;
import io.github.mateussilva.hotelmanagement.hotel.controller.dto.*;
import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.hotel.mapper.HotelMapper;
import io.github.mateussilva.hotelmanagement.hotel.projections.HotelDetailsProjection;
import io.github.mateussilva.hotelmanagement.hotel.projections.HotelMinProjection;
import io.github.mateussilva.hotelmanagement.hotel.service.HotelService;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonUpdateDTO;
import io.github.mateussilva.hotelmanagement.people.domain.Person;
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
@RequestMapping("api/v1/hotels")
public class HotelController {

    private final HotelService service;
    private final HotelMapper mapper;
    private final HotelMinModelAssembler modelAssembler;

    public HotelController(HotelService service, HotelMapper mapper, HotelMinModelAssembler modelAssembler) {
        this.service = service;
        this.mapper = mapper;
        this.modelAssembler = modelAssembler;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<HotelDetailsDTO> findByUuid(@PathVariable UUID uuid) {
        HotelDetailsProjection hotel = service.findByUuid(uuid);
        return ResponseEntity.ok(mapper.toDetailsDTO(hotel));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<HotelMinDTO>>> findAll(
            HotelFilterDTO filter,
            Pageable pageable,
            PagedResourcesAssembler<HotelMinDTO> assembler) {

        Page<HotelMinProjection> hotel = service.findAll(filter, pageable);
        Page<HotelMinDTO> page = hotel.map(mapper::toMinDTO);
        PagedModel<EntityModel<HotelMinDTO>> model = assembler.toModel(page, modelAssembler);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<HotelDetailsDTO> insert(@RequestBody @Valid HotelDTO dto) {
        Hotel hotel = service.insert(dto);
        HotelDetailsDTO hotelDTO = mapper.toDTO(hotel);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(hotel.getUuid())
                .toUri();
        return ResponseEntity.created(uri).body(hotelDTO);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Void> update(@PathVariable UUID uuid, @RequestBody @Valid HotelUpdateDTO dto) {
        service.update(uuid, dto);
        return ResponseEntity.noContent().build();
    }
}
