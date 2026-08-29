package io.github.mateussilva.hotelmanagement.hotel.mapper;

import io.github.mateussilva.hotelmanagement.hotel.controller.dto.HotelDTO;
import io.github.mateussilva.hotelmanagement.hotel.controller.dto.HotelDetailsDTO;
import io.github.mateussilva.hotelmanagement.hotel.controller.dto.HotelMinDTO;
import io.github.mateussilva.hotelmanagement.hotel.domain.CNPJ;
import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.hotel.projections.HotelDetailsProjection;
import io.github.mateussilva.hotelmanagement.hotel.projections.HotelMinProjection;
import io.github.mateussilva.hotelmanagement.shared.Email;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    default Hotel toEntity(HotelDTO dto) {
        return Hotel.of(
                dto.name(), dto.description(), dto.type(), dto.rating(), dto.defaultBoardBasis(), new CNPJ(dto.document()),
                dto.amenities(), dto.phoneNumber(), dto.mobileNumber(), new Email(dto.email())
        );
    }

    HotelDetailsDTO toDTO(Hotel entity);

    HotelMinDTO toMinDTO(HotelMinProjection projection);
    HotelDetailsDTO toDetailsDTO(HotelDetailsProjection projection);

    default String map(Email email) {
        return email != null ? email.getValue() : null;
    }

    default String map(CNPJ cnpj) {
        return cnpj != null ? cnpj.getValue() : null;
    }
}
