package io.github.mateussilva.hotelmanagement.hotel.controller.assembler;

import io.github.mateussilva.hotelmanagement.hotel.controller.HotelController;
import io.github.mateussilva.hotelmanagement.hotel.controller.dto.HotelMinDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HotelMinModelAssembler implements RepresentationModelAssembler<HotelMinDTO, EntityModel<HotelMinDTO>> {

    @Override
    public EntityModel<HotelMinDTO> toModel(HotelMinDTO dto) {
        return EntityModel.of(
                dto,
                linkTo(
                        methodOn(HotelController.class)
                                .findByUuid(dto.uuid())
                ).withSelfRel()
        );
    }

}
