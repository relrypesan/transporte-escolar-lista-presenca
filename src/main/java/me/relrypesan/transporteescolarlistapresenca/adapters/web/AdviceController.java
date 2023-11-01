package me.relrypesan.transporteescolarlistapresenca.adapters.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.ErrorDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.FieldErrorDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class AdviceController {

    public static final String MSG_ERRO_NAO_TRATADO = "Ocorreu um erro não tratado.";
    public static final String MSG_ERRO_INESPERADO = "Ocorreu um erro inesperado.";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handlerBusinessException(BusinessException exception) {
        log.error(exception.getMessage(), exception);
        var errorResponse = ErrorDto.builder()
                .status(exception.getStatus().value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(exception.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handlerHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error(MSG_ERRO_INESPERADO, exception);
        var errorResponse = ErrorDto.builder()
                .status(400)
                .message(MSG_ERRO_INESPERADO)
                .build();

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) exception.getCause();
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                var description = String.format("Valor do enum inválido! O Valor deve ser um dos: %s",
                        Arrays.toString(ife.getTargetType().getEnumConstants()));
                FieldErrorDto fed = FieldErrorDto.builder()
                        .name(ife.getPath().get(ife.getPath().size()-1).getFieldName())
                        .value(ife.getValue().toString())
                        .description(description)
                        .build();
                errorResponse.addField(fed);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handlerBusinessException(BadCredentialsException exception) {
        log.error("Erro de credenciais", exception);
        var errorResponse = ErrorDto.builder()
                .status(403)
                .message("Erro de credenciais")
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handlerBusinessException(Exception exception) {
        log.error(MSG_ERRO_NAO_TRATADO, exception);
        var errorResponse = ErrorDto.builder()
                .status(500)
                .message(MSG_ERRO_NAO_TRATADO)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}
