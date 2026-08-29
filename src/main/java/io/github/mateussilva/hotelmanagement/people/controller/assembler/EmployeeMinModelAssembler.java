package io.github.mateussilva.hotelmanagement.people.controller.assembler;

import io.github.mateussilva.hotelmanagement.people.controller.EmployeeController;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeMinDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeMinModelAssembler implements RepresentationModelAssembler<EmployeeMinDTO, EntityModel<EmployeeMinDTO>> {

    @Override
    public EntityModel<EmployeeMinDTO> toModel(EmployeeMinDTO dto) {
        return EntityModel.of(
                dto, linkTo(
                        methodOn(EmployeeController.class)
                                .findByUuid(dto.uuid()))
                        .withSelfRel());
    }

}
