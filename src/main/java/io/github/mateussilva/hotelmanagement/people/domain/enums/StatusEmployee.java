package io.github.mateussilva.hotelmanagement.people.domain.enums;

import io.github.mateussilva.hotelmanagement.people.domain.exception.InvalidEmployeeException;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Map;

@Getter
public enum StatusEmployee {
    ACTIVE("Ativo"),
    ON_LEAVE("Em licença"),
    TERMINATED("Contrato encerrado");

    private final String description;

    StatusEmployee(String description) {
        this.description = description;
    }

    private static final Map<StatusEmployee, EnumSet<StatusEmployee>> TRANSITIONS;
    static {
        TRANSITIONS = Map.of(
                ACTIVE, EnumSet.of(ON_LEAVE, TERMINATED),
                ON_LEAVE, EnumSet.of(ACTIVE, TERMINATED),
                TERMINATED, EnumSet.noneOf(StatusEmployee.class)
        );
    }

    public boolean canTransitionTo(StatusEmployee nextStatus) {
        if (nextStatus == null) return false;

        EnumSet<StatusEmployee> validTransitions = TRANSITIONS.get(this);

        return validTransitions != null && validTransitions.contains(nextStatus);
    }

    public StatusEmployee transitionTo(StatusEmployee nextStatus) {
        if (nextStatus == null)
            throw new InvalidEmployeeException("O novo status deve ser informado");

        if (!canTransitionTo(nextStatus))
            throw new InvalidEmployeeException(
                    "Transição de status inválida. O usuário não pode mover para o status solicitado a partir do status atual.");

        return nextStatus;
    }
}
