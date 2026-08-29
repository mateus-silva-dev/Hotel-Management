package io.github.mateussilva.hotelmanagement.hotel.domain.enums;

import lombok.Getter;

@Getter
public enum Amenity {
    WIFI("Wi-Fi Gratuito"),
    SWIMMING_POOL("Piscina"),
    GYM("Academia"),
    SPA("Spa"),
    PARKING("Estacionamento"),
    PET_FRIENDLY("Aceita Animais (Pet Friendly)"),
    ROOM_SERVICE_24H("Serviço de Quarto 24h"),
    CONCIERGE("Concierge");

    private final String description;

    Amenity(String description) {
        this.description = description;
    }

}
