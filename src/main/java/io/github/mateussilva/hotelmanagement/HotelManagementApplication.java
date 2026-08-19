package io.github.mateussilva.hotelmanagement;

import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.user.domain.CPF;
import io.github.mateussilva.hotelmanagement.user.domain.Employee;
import io.github.mateussilva.hotelmanagement.user.domain.JobPosition;
import io.github.mateussilva.hotelmanagement.user.domain.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.Month;

@SpringBootApplication
public class HotelManagementApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Person person = Person.of(
                "Carla",
                "Batista Silva",
                new CPF("658.242.178-66"),
                LocalDate.of(2003, Month.DECEMBER, 6),
                new Email("usuario@email.com"),
                "1142010204",
                "11989515383"
        );

        Employee employee = Employee.of(
                "01-2026-0001",
                LocalDate.of(2026, Month.MAY, 20),
                "RECEPCIONISTA"
        );

        JobPosition jobPosition = JobPosition.of(
                "RECEPCIONISTA"
        );


        System.out.println("\nPerson:");
        System.out.printf("""
                UUID:               %s
                First Name:         %s
                Surname:            %s
                Document:           %s
                birthDate:          %s
                Email:              %s
                Phone Number:       %s
                Mobile Number:      %s
                """,
                person.getUuid(), person.getFirstName(), person.getSurname(), person.getDocument().getValue(), person.getBirthDate(),
                person.getEmail().getValue(), person.getPhoneNumber(), person.getMobileNumber());
        System.out.println("Person created successfully!");

        System.out.println("\nEmployee:");
        System.out.printf("""
                UUID:                   %s
                Registration Code:      %s
                Hire Date:              %s
                Dismissal Date:         %s
                Status:                 %s
                Job Title:              %s
                """,
                employee.getUuid(), employee.getRegistrationCode(), employee.getHireDate(), employee.getDismissalDate(),
                employee.getStatus(), employee.getJobTitle());
        System.out.println("Employee created successfully!");

        System.out.println("\nJob Position:");
        System.out.printf("""
                UUID:      %s
                Name:      %s
                """,
                jobPosition.getUuid(), jobPosition.getName());
        System.out.println("Job Position created successfully!");
    }
}
