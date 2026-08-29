package io.github.mateussilva.hotelmanagement.hotel.domain;

import io.github.mateussilva.hotelmanagement.hotel.domain.enums.*;
import io.github.mateussilva.hotelmanagement.hotel.domain.exception.InvalidHotelException;
import io.github.mateussilva.hotelmanagement.people.domain.Employee;
import io.github.mateussilva.hotelmanagement.shared.Email;
import io.github.mateussilva.hotelmanagement.shared.doc.Generated;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_hotel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Hotel {

    private static final String NAME_REQUIRED_MESSAGE = "Um nome deve ser informado";
    private static final String SIZE = "O nome deve ter entre %d e %d caracteres";
    private static final String DESCRIPTION_REQUIRED_MESSAGE = "Uma descrição deve ser informado";
    private static final String TYPE_REQUIRED_MESSAGE = "O tipo do hotel deve ser informado";
    private static final String RATING_REQUIRED_MESSAGE = "A avaliação do hotel deve ser informado";
    private static final String BOARD_BASIS_REQUIRED_MESSAGE = "Um tipo de hospedagem deve ser informado";
    private static final String DOCUMENT_REQUIRED_MESSAGE = "Um documento deve ser informado";
    private static final String AMENITIES_REQUIRED_MESSAGE = "Informe ao menos uma comodidade";
    private static final String AMENITIES_MIN_REQUIRED_MESSAGE = "O hotel deve ter pelo menos uma comodidade";
    private static final String EMAIL_REQUIRED_MESSAGE = "Um email deve ser informado";
    private static final String CONTACT_REQUIRED_MESSAGE = "Informe um número de telefone ou celular";
    private static final String CONTACT_INVALID_FORMAT = "Formato do contato inválido";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private UUID uuid;

    @Column
    private String name;
    @Column
    private String description;
    @Enumerated(EnumType.STRING)
    private HotelType type;
    @Enumerated(EnumType.STRING)
    private HotelRating rating;
    @Enumerated(EnumType.STRING)
    private BoardBasis defaultBoardBasis;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "document"))
    private CNPJ document;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tb_hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "amenity")
    private Set<Amenity> amenities = new HashSet<>();

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email email;
    @Column
    private String phoneNumber;
    @Column
    private String mobileNumber;

    @Column
    private boolean active = true;


    @Generated
    private Hotel(String name, String description, HotelType type, HotelRating rating, BoardBasis defaultBoardBasis, CNPJ document, Set<Amenity> amenities, String phoneNumber, String mobileNumber, Email email) {
        String cleanedPhoneNumber = cleanNumber(phoneNumber);
        String cleanedMobileNumber = cleanNumber(mobileNumber);

        validateCreation(name, description, type, rating, defaultBoardBasis, document, amenities, email, phoneNumber, mobileNumber);
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.type = type;
        this.rating = rating;
        this.defaultBoardBasis = defaultBoardBasis;
        this.document = document;
        this.amenities = amenities;
        this.phoneNumber = cleanedPhoneNumber;
        this.mobileNumber = cleanedMobileNumber;
        this.email = email;
    }

    public static Hotel of(String name, String description, HotelType type, HotelRating rating, BoardBasis defaultBoardBasis, CNPJ document, Set<Amenity> amenities, String phoneNumber, String mobileNumber, Email email) {
        return new Hotel(name, description, type, rating, defaultBoardBasis, document, amenities, phoneNumber, mobileNumber, email);
    }


    public void updateName(String newName) {
        requireText(newName, NAME_REQUIRED_MESSAGE, 100);
        if (Objects.equals(newName, this.name)) return;
        this.name = newName;
    }

    public void updateDescription(String newDescription) {
        requireText(newDescription, DESCRIPTION_REQUIRED_MESSAGE, 1000);
        if (Objects.equals(newDescription, this.description)) return;
        this.description = newDescription;
    }

    public void updateType(HotelType newType) {
        requireNonNull(newType, TYPE_REQUIRED_MESSAGE);
        if (Objects.equals(newType, this.type)) return;
        this.type = newType;
    }

    public void updateRating(HotelRating newRating) {
        requireNonNull(newRating, RATING_REQUIRED_MESSAGE);
        if (Objects.equals(newRating, this.rating)) return;
        this.rating = newRating;
    }

    public void updateDefaultBoardBasis(BoardBasis newBoardBasis) {
        requireNonNull(newBoardBasis, BOARD_BASIS_REQUIRED_MESSAGE);
        if (Objects.equals(newBoardBasis, this.defaultBoardBasis)) return;
        this.defaultBoardBasis = newBoardBasis;
    }

    public void addAmenity(Amenity newAmenity) {
        requireNonNull(newAmenity, AMENITIES_REQUIRED_MESSAGE);
        if (this.amenities.contains(newAmenity)) return;
        this.amenities.add(newAmenity);
    }

    public void removeAmenity(Amenity amenityToRemove) {
        requireNonNull(amenityToRemove, AMENITIES_REQUIRED_MESSAGE);
        if (!this.amenities.contains(amenityToRemove)) return;
        if (this.amenities.size() == 1)
            throw new InvalidHotelException(AMENITIES_MIN_REQUIRED_MESSAGE);
        this.amenities.remove(amenityToRemove);
    }

    public void updateEmail(Email newEmail) {
        requireNonNull(newEmail, EMAIL_REQUIRED_MESSAGE);
        if (Objects.equals(newEmail, this.email)) return;
        this.email = newEmail;
    }

    public void updatePhoneNumber(String newPhoneNumber) {
        String cleanedPhoneNumber = cleanNumber(newPhoneNumber);

        if (isBlank(cleanedPhoneNumber) && this.mobileNumber == null)
            throw new InvalidHotelException(CONTACT_REQUIRED_MESSAGE);

        checkPhoneNumber(cleanedPhoneNumber);
        if (Objects.equals(cleanedPhoneNumber, this.phoneNumber)) return;
        this.phoneNumber = cleanedPhoneNumber;
    }

    public void updateMobileNumber(String newMobileNumber) {
        String cleanedMobileNumber = cleanNumber(newMobileNumber);

        if (isBlank(cleanedMobileNumber) && this.phoneNumber == null)
            throw new InvalidHotelException(CONTACT_REQUIRED_MESSAGE);

        checkMobileNumber(cleanedMobileNumber);
        if (Objects.equals(cleanedMobileNumber, this.mobileNumber)) return;
        this.mobileNumber = cleanedMobileNumber;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }


    private static void validateCreation(String name, String description, HotelType type, HotelRating rating, BoardBasis defaultBoardBasis, CNPJ document, Set<Amenity> amenities, Email email, String phoneNumber, String mobileNumber) {
        requireText(name, NAME_REQUIRED_MESSAGE, 100);
        requireText(description, DESCRIPTION_REQUIRED_MESSAGE, 1000);
        requireNonNull(type, TYPE_REQUIRED_MESSAGE);
        requireNonNull(rating, RATING_REQUIRED_MESSAGE);
        requireNonNull(defaultBoardBasis, BOARD_BASIS_REQUIRED_MESSAGE);
        requireNonNull(document, DOCUMENT_REQUIRED_MESSAGE);
        requireNonNull(amenities, AMENITIES_REQUIRED_MESSAGE);
        requireNonNull(email, EMAIL_REQUIRED_MESSAGE);

        if (isBlank(phoneNumber) && isBlank(mobileNumber))
            throw new InvalidHotelException(CONTACT_REQUIRED_MESSAGE);

        checkPhoneNumber(phoneNumber);
        checkMobileNumber(mobileNumber);
    }

    private static String cleanNumber(String number) {
        return number != null ? number.replaceAll("\\D", "") : null;
    }

    private static void requireText(String value, String message, int maxLength) {
        if (isBlank(value))
            throw new InvalidHotelException(message);
        if (value.length() < 3 || value.length() > maxLength)
            throw new InvalidHotelException(SIZE.formatted(3, maxLength));
    }

    private static void requireNonNull(Object value, String message) {
        if (value == null)
            throw new InvalidHotelException(message);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static void checkPhoneNumber(String phoneNumber) {
        if (!isBlank(phoneNumber) && phoneNumber.length() != 10)
            throw new InvalidHotelException(CONTACT_INVALID_FORMAT);
    }

    private static void checkMobileNumber(String mobileNumber) {
        if (!isBlank(mobileNumber) && mobileNumber.length() != 11)
            throw new InvalidHotelException(CONTACT_INVALID_FORMAT);
    }


    @Generated
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Hotel hotel)) return false;

        return Objects.equals(uuid, hotel.uuid);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
