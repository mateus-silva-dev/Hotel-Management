package io.github.mateussilva.hotelmanagement.shared.mapper;

import io.github.mateussilva.hotelmanagement.hotel.domain.CNPJ;
import io.github.mateussilva.hotelmanagement.people.domain.CPF;
import io.github.mateussilva.hotelmanagement.shared.Email;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ValueObjectMapper {

    default String map(Email email) {
        return email == null ? null : email.getValue();
    }

}
