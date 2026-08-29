package io.github.mateussilva.hotelmanagement.people.mapper;

import io.github.mateussilva.hotelmanagement.people.controller.dto.jobposition.JobPositionDTO;
import io.github.mateussilva.hotelmanagement.people.domain.JobPosition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobPositionMapper {

    JobPositionDTO toDTO(JobPosition entity);
    default JobPosition toEntity(JobPositionDTO dto) {
        return JobPosition.of(dto.name());
    }
}
