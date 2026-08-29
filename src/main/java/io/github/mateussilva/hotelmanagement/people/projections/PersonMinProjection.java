package io.github.mateussilva.hotelmanagement.people.projections;

import java.util.UUID;

public interface PersonMinProjection {
    UUID getUuid();
    String getFirstName();
    String getSurname();
    String getEmail();
}
