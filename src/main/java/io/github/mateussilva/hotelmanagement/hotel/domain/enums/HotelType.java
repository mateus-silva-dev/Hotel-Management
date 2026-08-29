package io.github.mateussilva.hotelmanagement.hotel.domain.enums;

import lombok.Getter;

@Getter
public enum HotelType {
    HOTEL("Hotel Tradicional"),
    RESORT("Resort"),
    FARM_HOTEL("Hotel-Fazenda"),
    GUEST_HOUSE("Pousada"),
    HOSTEL("Hostel / Albergue"),
    APART_HOTEL("Apart-hotel / Flat"),
    BED_AND_BREAKFAST("Cama e Café"),
    HISTORIC_HOTEL("Hotel Histórico");

    private final String description;

    HotelType(String description) {
        this.description = description;
    }

}