package io.github.mateussilva.hotelmanagement.people.controller.assembler;

import io.github.mateussilva.hotelmanagement.people.controller.PersonController;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonMinDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PersonMinModelAssembler implements RepresentationModelAssembler<PersonMinDTO, EntityModel<PersonMinDTO>> {

    @Override
    public EntityModel<PersonMinDTO> toModel(PersonMinDTO dto) {
        return EntityModel.of(
                dto,
                linkTo(
                        methodOn(PersonController.class)
                                .findDetailsByUuid(dto.uuid())
                ).withSelfRel()
        );
    }

}
