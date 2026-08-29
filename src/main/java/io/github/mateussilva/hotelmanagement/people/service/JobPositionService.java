package io.github.mateussilva.hotelmanagement.people.service;

import io.github.mateussilva.hotelmanagement.people.controller.dto.jobposition.JobPositionDTO;
import io.github.mateussilva.hotelmanagement.people.domain.JobPosition;
import io.github.mateussilva.hotelmanagement.people.mapper.JobPositionMapper;
import io.github.mateussilva.hotelmanagement.people.repository.JobPositionRepository;
import io.github.mateussilva.hotelmanagement.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class JobPositionService {

    private final JobPositionRepository repository;
    private final JobPositionMapper mapper;

    public JobPositionService(JobPositionRepository repository, JobPositionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public JobPosition findDetailsByUuid(UUID uuid) {
        return repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<JobPosition> findAllDetails(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public JobPosition create(JobPositionDTO dto) {
        return repository.save(mapper.toEntity(dto));
    }

    @Transactional
    public void activate(UUID uuid) {
        findEntityByUuidOrThrow(uuid).activate();
    }

    @Transactional
    public void deactivate(UUID uuid) {
        findEntityByUuidOrThrow(uuid).deactivate();
    }

    private JobPosition findEntityByUuidOrThrow(UUID uuid) {
        return repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

}
