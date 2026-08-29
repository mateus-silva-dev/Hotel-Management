package io.github.mateussilva.hotelmanagement.people.mapper;

import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonDetailsDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonMinDTO;
import io.github.mateussilva.hotelmanagement.people.projections.PersonDetailsProjection;
import io.github.mateussilva.hotelmanagement.people.projections.PersonMinProjection;
import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.people.domain.CPF;
import io.github.mateussilva.hotelmanagement.people.domain.Person;
import io.github.mateussilva.hotelmanagement.shared.mapper.ValueObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ValueObjectMapper.class)
public interface PersonMapper {

    default Person toEntity(PersonDTO dto) {
        return Person.of(
                dto.firstName(), dto.surname(), new CPF(dto.document()), dto.birthDate(),
                new Email(dto.email()), dto.phoneNumber(), dto.mobileNumber());
    }

    @Mapping(target = "document", source = "document.value")
    @Mapping(target = "email", source = "email.value")
    PersonDTO toDTO(Person entity);

    PersonMinDTO toMinDTO(PersonMinProjection projection);
    PersonDetailsDTO toDetailsDTO(PersonDetailsProjection projection);

    default String map(CPF cpf) {
        return cpf == null ? null : cpf.getValue();
    }
}
