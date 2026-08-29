package io.github.mateussilva.hotelmanagement.hotel.domain.enums;

import lombok.Getter;

@Getter
public enum BoardBasis {
    ROOM_ONLY("Só o Quarto (Sem Alimentação)"),
    BED_AND_BREAKFAST("Café da Manhã Incluso"),
    HALF_BOARD("Meia Pensão"),
    FULL_BOARD("Pensão Completa"),
    ALL_INCLUSIVE("Tudo Incluído (All Inclusive)");

    private final String description;

    BoardBasis(String description) {
        this.description = description;
    }
}
