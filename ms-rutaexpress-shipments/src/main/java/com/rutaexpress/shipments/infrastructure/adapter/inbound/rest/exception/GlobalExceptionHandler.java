package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.exception;

import com.rutaexpress.shipments.domain.exception.InvalidStatusTransitionException;
import com.rutaexpress.shipments.domain.exception.ShipmentNotFoundException;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

// Traduce cada excepción a su código HTTP con formato ProblemDetail (RFC 9457)
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404: el envío no existe
    @ExceptionHandler(ShipmentNotFoundException.class)
    public ProblemDetail handleNotFound(ShipmentNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, "Envío no encontrado", ex.getMessage());
    }

    // 409: transición de estado no permitida (choca con el estado actual)
    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ProblemDetail handleTransition(InvalidStatusTransitionException ex) {
        return problem(HttpStatus.CONFLICT, "Transición de estado no permitida", ex.getMessage());
    }

    // 400: falla @NotBlank / @Email / @NotNull del body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fields.put(err.getField(), err.getDefaultMessage()));
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Error de validación",
                "La solicitud contiene datos de entrada no válidos.");
        pd.setProperty("invalidFields", fields);
        return pd;
    }

    // 400: JSON mal formado o status que no existe en el enum
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex) {
        return problem(HttpStatus.BAD_REQUEST, "Cuerpo inválido",
                "Revise el JSON. status debe ser uno de " + Arrays.toString(ShipmentStatus.values()));
    }

    // 400: parámetro de la URL con tipo incorrecto (ej: ?status=XX o ?from=ayer)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return problem(HttpStatus.BAD_REQUEST, "Parámetro inválido",
                ex.getName() + " tiene un valor inválido: '" + ex.getValue() + "'");
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        if (status.is4xxClientError()) {
            log.warn("[HTTP {}] {}", status.value(), detail);
        }
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        return pd;
    }
}
