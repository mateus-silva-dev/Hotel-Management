package io.github.mateussilva.hotelmanagement.people.projections;

import java.time.LocalDate;
import java.util.UUID;

public interface PersonDetailsProjection {
    UUID getUuid();
    String getFirstName();
    String getSurname();
    String getDocument();
    LocalDate getBirthDate();
    String getEmail();
    String getPhoneNumber();
    String getMobileNumber();
}
