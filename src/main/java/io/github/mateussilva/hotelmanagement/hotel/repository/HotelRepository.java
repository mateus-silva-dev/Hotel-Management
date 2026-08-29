package io.github.mateussilva.hotelmanagement.hotel.repository;

import io.github.mateussilva.hotelmanagement.hotel.domain.Hotel;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.Amenity;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.BoardBasis;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelRating;
import io.github.mateussilva.hotelmanagement.hotel.domain.enums.HotelType;
import io.github.mateussilva.hotelmanagement.hotel.projections.HotelDetailsProjection;
import io.github.mateussilva.hotelmanagement.hotel.projections.HotelMinProjection;
import io.github.mateussilva.hotelmanagement.people.projections.PersonDetailsProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT obj FROM Hotel obj WHERE obj.uuid = :uuid")
    Optional<Hotel> findEntityByUuid(UUID uuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT h
    FROM Hotel h
    WHERE h.uuid = :uuid
    """)
    Optional<Hotel> findByUuidForUpdate(@Param("uuid") UUID uuid);

    @Query("""
    SELECT DISTINCT h
    FROM Hotel h
    LEFT JOIN FETCH h.amenities
    WHERE h.uuid = :uuid
    """)
    Optional<HotelDetailsProjection> findDetailsByUuid(UUID uuid);

    @Query("""
    SELECT
        h.uuid      AS uuid,
        h.name      AS name,
        h.type      AS type,
        h.rating    AS rating
    FROM Hotel h
    JOIN h.amenities amenity
    WHERE (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:type IS NULL OR h.type = :type)
        AND (:rating IS NULL OR h.rating = :rating)
        AND (:boardBasis IS NULL OR h.defaultBoardBasis = :boardBasis)
        AND amenity IN :amenities
    GROUP BY h.uuid, h.name, h.type, h.rating
    HAVING COUNT(DISTINCT amenity) = :amenitiesCount
    """)
    Page<HotelMinProjection> findAllMinWithAmenities(
            @Param("name") String name, @Param("type") HotelType type, @Param("rating") HotelRating rating,
            @Param("boardBasis") BoardBasis boardBasis, @Param("amenities") Set<Amenity> amenities, @Param("amenitiesCount") long amenitiesCount,
            Pageable pageable);

    @Query("""
    SELECT
        h.uuid      AS uuid,
        h.name      AS name,
        h.type      AS type,
        h.rating    AS rating
    FROM Hotel h
    WHERE (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:type IS NULL OR h.type = :type)
        AND (:rating IS NULL OR h.rating = :rating)
        AND (:boardBasis IS NULL OR h.defaultBoardBasis = :boardBasis)
    """)
    Page<HotelMinProjection> findAllMinWithFilters(
            @Param("name") String name, @Param("type") HotelType type, @Param("rating") HotelRating rating,
            @Param("boardBasis") BoardBasis boardBasis, Pageable pageable);
}
