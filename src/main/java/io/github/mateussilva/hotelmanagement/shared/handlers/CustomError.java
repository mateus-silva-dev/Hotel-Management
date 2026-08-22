package io.github.mateussilva.hotelmanagement.shared.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record CustomError(
        Instant timestamp,
        int status,
        String error,
        String path,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, List<String>> errors
) {
    public static CustomError of(int status, String error, String path) {
        return new CustomError(Instant.now(), status, error, path, null);
    }

    public static CustomError validation(String error, String path, BindingResult result) {
        Map<String, List<String>> errors = result.getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Error message unavailable"),
                                Collectors.toList())
                ));

        return new CustomError(
                Instant.now(),
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                error,
                path,
                errors
        );
    }
}
