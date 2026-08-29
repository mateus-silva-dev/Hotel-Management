package io.github.mateussilva.hotelmanagement.people.controller.assembler;

import io.github.mateussilva.hotelmanagement.people.controller.EmployeeController;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeDetailsDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeMinDTO;
import io.github.mateussilva.hotelmanagement.people.domain.enums.StatusEmployee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeDetailsModelAssembler implements RepresentationModelAssembler<EmployeeDetailsDTO, EntityModel<EmployeeDetailsDTO>> {

    @Override
    public EntityModel<EmployeeDetailsDTO> toModel(EmployeeDetailsDTO dto) {
        EntityModel<EmployeeDetailsDTO> model = EntityModel.of(
                dto, linkTo(
                        methodOn(EmployeeController.class)
                                .findByUuid(dto.uuid()))
                        .withSelfRel());

        if(dto.status().canTransitionTo(StatusEmployee.ACTIVE)) {
            model.add(linkTo(
                    methodOn(EmployeeController.class)
                            .activate(dto.uuid()))
                    .withRel("activate")
            );
        }

        if(dto.status().canTransitionTo(StatusEmployee.ON_LEAVE)) {
            model.add(linkTo(
                    methodOn(EmployeeController.class)
                            .putOnLeave(dto.uuid()))
                    .withRel("put-on-leave")
            );
        }

        if(dto.status().canTransitionTo(StatusEmployee.TERMINATED)) {
            model.add(linkTo(
                    methodOn(EmployeeController.class)
                            .terminate(dto.uuid(), null))
                    .withRel("terminate")
            );
        }

        return model;
    }

}
