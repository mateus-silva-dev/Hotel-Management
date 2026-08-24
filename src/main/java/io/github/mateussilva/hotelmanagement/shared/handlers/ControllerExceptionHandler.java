package io.github.mateussilva.hotelmanagement.shared.handlers;

import io.github.mateussilva.hotelmanagement.shared.exception.BusinessRulesException;
import io.github.mateussilva.hotelmanagement.shared.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.HtmlUtils;

@SuppressWarnings("JvmTaintAnalysis")
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomError> handleErroGenerico(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(status)
                .body(CustomError.of(status.value(), "Erro interno no servidor", request.getRequestURI()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(CustomError.of(status.value(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomError> database(DataIntegrityViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String path = HtmlUtils.htmlEscape(request.getRequestURI());

        String message = e.getMostSpecificCause().getMessage().toLowerCase();

        if (message.contains("uk_person_email"))
            return ResponseEntity.status(status)
                    .body(CustomError.of(status.value(), "Este e-mail já está cadastrado", path));

        if (message.contains("uk_person_document"))
            return ResponseEntity.status(status)
                    .body(CustomError.of(status.value(), "Este documento já está cadastrado", request.getRequestURI()));

        return ResponseEntity
                .status(status)
                .body(CustomError.of(status.value(), "Erro de integridade de dados", request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> argumentNotValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        return ResponseEntity
                .unprocessableContent()
                .body(CustomError.validation("Erro de validação nos campos enviados", request.getRequestURI(), e.getBindingResult()));
    }

    @ExceptionHandler(BusinessRulesException.class)
    public ResponseEntity<CustomError> businessException(BusinessRulesException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        return ResponseEntity
                .status(status)
                .body(CustomError.of(status.value(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomError> httpMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(CustomError.of(status.value(), "O corpo da requisição possui um formato JSON inválido ou malformado", request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomError> methodArgumentTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(CustomError.of(status.value(), "Parâmetro informado possui formato inválido", request.getRequestURI()));
    }

}
