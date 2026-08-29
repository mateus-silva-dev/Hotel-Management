package io.github.mateussilva.hotelmanagement.people.domain;

import io.github.mateussilva.hotelmanagement.shared.doc.Generated;
import io.github.mateussilva.hotelmanagement.people.domain.exception.InvalidJobPositionException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_job_position")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JobPosition {

    private static final String NAME_REQUIRED_MESSAGE = "Nome inválido";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID uuid;

    @Column(unique = true)
    private String name;

    private boolean active = true;

    @OneToMany(mappedBy = "jobPosition")
    private Set<Employee> employees = new HashSet<>();

    @Generated
    private JobPosition(String name) {
        this.uuid = UUID.randomUUID();

        if (name == null || name.isBlank() || name.length() < 2)
            throw new InvalidJobPositionException(NAME_REQUIRED_MESSAGE);

        this.name = name.toUpperCase();
    }

    public static JobPosition of(String name) {
        return new JobPosition(name);
    }


    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }


    @Generated
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof JobPosition that)) return false;

        return Objects.equals(getUuid(), that.getUuid());
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }
}
