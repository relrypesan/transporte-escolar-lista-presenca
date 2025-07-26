package me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void criarBusinessException_ComMensagem_DeveDefinirMensagemEStatusPadrao() {
        String mensagem = "Erro de negócio";
        
        BusinessException exception = new BusinessException(mensagem);
        
        assertEquals(mensagem, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void criarBusinessException_ComMensagemEStatus_DeveDefinirMensagemEStatus() {
        String mensagem = "Recurso não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        
        BusinessException exception = new BusinessException(status, mensagem);
        
        assertEquals(mensagem, exception.getMessage());
        assertEquals(status, exception.getStatus());
    }

    @Test
    void criarBusinessException_ComMensagemECausa_DeveDefinirMensagemCausaEStatusPadrao() {
        String mensagem = "Erro de negócio";
        Throwable causa = new RuntimeException("Causa raiz");
        
        BusinessException exception = new BusinessException(mensagem, causa);
        
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void criarBusinessException_ComStatusMensagemECausa_DeveDefinirTodosOsValores() {
        String mensagem = "Erro interno";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Throwable causa = new RuntimeException("Causa raiz");
        
        BusinessException exception = new BusinessException(status, mensagem, causa);
        
        assertEquals(mensagem, exception.getMessage());
        assertEquals(status, exception.getStatus());
        assertEquals(causa, exception.getCause());
    }
}
