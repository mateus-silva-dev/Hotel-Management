package io.github.mateussilva.hotelmanagement.hotel.domain.enums;

import lombok.Getter;

@Getter
public enum HotelRating {
    FIVE_STARS("5 estrelas"),
    FOUR_STARS("4 estrelas"),
    THREE_STARS("3 estrelas"),
    TWO_STARS("2 estrelas"),
    ONE_STAR("5 estrela");

    private final String description;

    HotelRating(String description) {
        this.description = description;
    }
}
