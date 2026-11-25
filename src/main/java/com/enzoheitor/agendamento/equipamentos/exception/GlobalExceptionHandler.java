package com.enzoHeitor.agendamento.equipamentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Aula 6 - tratamento de exceções.
 * As regras de negócio já lançam ResponseStatusException (400, 404, 409) e o Spring devolve
 * {"status", "error", "message", ...} (message habilitada em application.properties).
 * Aqui tratamos o caso que ficaria com texto técnico: JSON/data em formato inválido.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> corpoInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", "Dados inválidos: confira os campos e use datas no formato AAAA-MM-DD"));
    }
}
