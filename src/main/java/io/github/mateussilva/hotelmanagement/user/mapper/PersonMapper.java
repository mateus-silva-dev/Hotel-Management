package io.github.mateussilva.hotelmanagement.user.mapper;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.user.controller.dto.PersonDTO;
import io.github.mateussilva.hotelmanagement.user.domain.CPF;
import io.github.mateussilva.hotelmanagement.user.domain.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    default Person toEntity(PersonDTO dto) {
        return Person.of(
                dto.firstName(), dto.surname(), new CPF(dto.document()), dto.birthDate(),
                new Email(dto.email()), dto.phoneNumber(), dto.mobileNumber());
    }

    PersonDTO toDTO(Person entity);

    default String map(CPF cpf) {
        return cpf == null ? null : cpf.getValue();
    }

    default String map(Email email) {
        return email == null ? null : email.getValue();
    }
}
