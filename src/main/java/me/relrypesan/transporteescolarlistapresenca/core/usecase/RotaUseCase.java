package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Rota;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.rota.ConsultarRotaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.rota.DeletarRotaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.rota.SalvarRotaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.usuario.ConsultarUsuarioPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RotaUseCase {
    private final ConsultarRotaPort consultarRotaPort;
    private final SalvarRotaPort salvarRotaPort;
    private final DeletarRotaPort deletarRotaPort;
    private final ConsultarUsuarioPort consultarUsuarioPort;

    public Rota cadastrarNovaRota(Rota rota) {
        if (rota.getUsuario() == null || rota.getUsuario().getId() == null) throw new BusinessException("Deve ser informado o ID de usuario para cadastrar a Rota");

        var usuarioOptional = consultarUsuarioPort.consultar(rota.getUsuario().getId());
        if (usuarioOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID usuario não encontrado");

        return salvarRotaPort.cadastrar(rota);
    }

    public Page<Rota> listarRotas(Pageable pageable, Map<String, String> filters) {
        return consultarRotaPort.paginar(pageable, filters);
    }

    public List<Rota> listarRotasPorUsuario(String idUsuario) {
        var usuarioOptional = consultarUsuarioPort.consultar(idUsuario);
        if (usuarioOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID usuario não encontrado");
        return consultarRotaPort.consultarRotasPorUsuario(idUsuario);
    }

    public Rota consultarRota(String idRota) {
        var rotaOptional = consultarRotaPort.consultar(idRota);
        if (rotaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID rota não encontrado");
        return rotaOptional.get();
    }

    public Rota atualizarRota(Rota rota) {
        if (rota.getUsuario() == null || rota.getUsuario().getId() == null) throw new BusinessException("Deve ser informado o ID usuario para atualizar a Rota");

        var usuarioOptional = consultarUsuarioPort.consultar(rota.getUsuario().getId());
        if (usuarioOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID usuario não encontrado");

        return salvarRotaPort.atualizar(rota);
    }

    public void deletarRota(String idRota) {
        deletarRotaPort.deletar(Rota.builder().id(idRota).build());
    }

}
