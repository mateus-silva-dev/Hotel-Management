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
    public JobPosition findByUuid(UUID uuid) {
        return repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<JobPosition> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public JobPosition insert(JobPositionDTO dto) {
        JobPosition job = mapper.toEntity(dto);
        repository.save(job);
        return job;
    }

    @Transactional
    public void activate(UUID uuid) {
        JobPosition job = findEntityByUuidOrThrow(uuid);
        job.activate();
    }

    @Transactional
    public void deactivate(UUID uuid) {
        JobPosition job = findEntityByUuidOrThrow(uuid);
        job.deactivate();
    }

    private JobPosition findEntityByUuidOrThrow(UUID uuid) {
        return repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

}
