package io.github.mateussilva.hotelmanagement.hotel.service;

import io.github.mateussilva.hotelmanagement.hotel.HotelLookup;
import io.github.mateussilva.hotelmanagement.hotel.controller.dto.HotelDTO;
import io.github.mateussilva.hotelmanagement.hotel.controller.dto.HotelFilterDTO;
import io.github.mateussilva.hotelmanagement.hotel.controller.dto.HotelUpdateDTO;
import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.Amenity;
import io.github.mateussilva.hotelmanagement.hotel.mapper.HotelMapper;
import io.github.mateussilva.hotelmanagement.hotel.projections.HotelDetailsProjection;
import io.github.mateussilva.hotelmanagement.hotel.projections.HotelMinProjection;
import io.github.mateussilva.hotelmanagement.hotel.repository.HotelRepository;
import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class HotelService implements HotelLookup {

    private final HotelRepository repository;
    private final HotelMapper mapper;

    public HotelService(HotelRepository repository, HotelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel findEntityByUuid(UUID uuid) {
        return repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @Transactional
    public Hotel findEntityByUuidForUpdate(UUID uuid) {
        return repository.findByUuidForUpdate(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public HotelDetailsProjection findByUuid(UUID uuid) {
        return repository.findDetailsByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<HotelMinProjection> findAll(HotelFilterDTO filter, Pageable pageable) {
        Set<Amenity> amenities = filter.amenities();

        if(amenities == null || amenities.isEmpty()) {
            return repository.findAllMinWithFilters(
                    filter.name(), filter.type(), filter.rating(), filter.boardBasis(), pageable);
        }

        return repository.findAllMinWithAmenities(
                filter.name(), filter.type(), filter.rating(), filter.boardBasis(), amenities, amenities.size(), pageable);
    }

    @Transactional
    public Hotel insert(HotelDTO dto) {
        Hotel hotel = mapper.toEntity(dto);
        repository.save(hotel);
        return hotel;
    }

    @Transactional
    public Hotel update(UUID uuid, HotelUpdateDTO dto) {
        Hotel hotel = repository.findEntityByUuid(uuid)
                .orElseThrow(ResourceNotFoundException::new);

        System.out.println("chegou na service");
        System.out.println("Nome do hotel encontrado: " + hotel.getName());
        System.out.println();
        System.out.println(dto);

        if (dto.newName() != null)
            hotel.updateName(dto.newName());
        if (dto.newDescription() != null)
            hotel.updateDescription(dto.newDescription());
        if (dto.newType() != null)
            hotel.updateType(dto.newType());
        if (dto.newRating() != null)
            hotel.updateRating(dto.newRating());
        if (dto.newBoardBasis() != null)
            hotel.updateDefaultBoardBasis(dto.newBoardBasis());
        if (dto.newEmail() != null) {
            Email email = new Email(dto.newEmail());
            hotel.updateEmail(email);
        }
        if (dto.newPhoneNumber() != null)
            hotel.updatePhoneNumber(dto.newPhoneNumber());
        if (dto.newMobileNumber() != null)
            hotel.updateMobileNumber(dto.newMobileNumber());

        return repository.save(hotel);
    }

}
