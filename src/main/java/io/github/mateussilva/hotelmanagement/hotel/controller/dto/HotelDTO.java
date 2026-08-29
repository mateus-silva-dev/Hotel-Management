package io.github.mateussilva.hotelmanagement.hotel.controller.dto;

import io.github.mateussilva.hotelmanagement.hotel.domain.enums.Amenity;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.BoardBasis;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelRating;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.Set;
import java.util.UUID;

public record HotelDTO(
        UUID uuid,

        @NotBlank(message = "Campo requerido")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,

        @NotBlank(message = "Campo requerido")
        @Size(min = 3, max = 1000, message = "A descrição deve ter entre 3 e 1000 caracteres")
        String description,

        @NotNull(message = "Campo requerido")
        HotelType type,

        @NotNull(message = "Campo requerido")
        HotelRating rating,

        @NotNull(message = "Campo requerido")
        BoardBasis defaultBoardBasis,

        @NotNull(message = "Campo requerido")
        @CNPJ
        String document,

        @NotNull(message = "Campo requerido")
        @NotEmpty(message = "Informe ao menos uma comodidade")
        Set<Amenity> amenities,

        @NotBlank(message = "Campo requerido")
        @Email
        String email,

        @Size(min = 10, max = 10, message = "O número de telefone deve ter 10 digítos")
        String phoneNumber,

        @Size(min = 11, max = 11, message = "O número de celular deve ter 11 digítos")
        String mobileNumber
) {

}
