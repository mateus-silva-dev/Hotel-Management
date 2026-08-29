package io.github.mateussilva.hotelmanagement.hotel;

import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;

import java.util.UUID;

public interface HotelLookup {
    Hotel findEntityByUuid(UUID uuid);
    Hotel findEntityByUuidForUpdate(UUID uuid);
}
