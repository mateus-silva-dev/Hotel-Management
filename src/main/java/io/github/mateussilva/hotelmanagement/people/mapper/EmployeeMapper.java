package io.github.mateussilva.hotelmanagement.people.mapper;

import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeCreateDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeDetailsDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.employee.EmployeeMinDTO;
import io.github.mateussilva.hotelmanagement.people.controller.dto.person.PersonDetailsDTO;
import io.github.mateussilva.hotelmanagement.people.domain.CPF;
import io.github.mateussilva.hotelmanagement.people.domain.Employee;
import io.github.mateussilva.hotelmanagement.people.domain.JobPosition;
import io.github.mateussilva.hotelmanagement.people.domain.Person;
import io.github.mateussilva.hotelmanagement.people.projections.EmployeeDetailsProjection;
import io.github.mateussilva.hotelmanagement.people.projections.EmployeeMinProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PersonMapper.class)
public interface EmployeeMapper {

    default Employee toEntity(Person person, Hotel hotel, JobPosition jobPosition, String registrationCode, EmployeeCreateDTO dto) {
        return Employee.of(
                person, hotel, jobPosition, registrationCode, dto.hireDate()
        );
    }

    @Mapping(target = "jobPosition", source = "jobPosition.uuid")
    @Mapping(target = "hotel", source = "hotel.uuid")
    EmployeeCreateDTO toDTO(Employee entity);

    EmployeeMinDTO toMinDTO(EmployeeMinProjection projection);

    EmployeeDetailsDTO toDetailsDTO(Employee entity);

    default EmployeeDetailsDTO toDetailsDTO(EmployeeDetailsProjection projection) {
        if (projection == null) {
            return null;
        }

        PersonDetailsDTO person = new PersonDetailsDTO(
                projection.getPersonUuid(),
                projection.getPersonFirstName(),
                projection.getPersonSurname(),
                projection.getPersonDocument(),
                projection.getPersonBirthDate(),
                projection.getPersonEmail(),
                projection.getPersonPhoneNumber(),
                projection.getPersonMobileNumber()
        );

        return new EmployeeDetailsDTO(
                projection.getUuid(),
                projection.getRegistrationCode(),
                projection.getHireDate(),
                projection.getDismissalDate(),
                projection.getJobTitle(),
                projection.getStatus(),
                person
        );
    }

    default String map(CPF cpf) {
        return cpf == null ? null : cpf.getValue();
    }

}
