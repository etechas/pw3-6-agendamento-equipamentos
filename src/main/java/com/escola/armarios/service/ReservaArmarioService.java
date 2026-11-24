package com.escola.armarios.service;

import com.escola.armarios.exception.ConflitoReservaException;
import com.escola.armarios.exception.RecursoNaoEncontradoException;
import com.escola.armarios.exception.RegraNegocioException;
import com.escola.armarios.model.ReservaArmario;
import com.escola.armarios.repository.ReservaArmarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaArmarioService {

    private final ReservaArmarioRepository repository;

    public List<ReservaArmario> listar() {
        return repository.findAll();
    }

    public ReservaArmario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada: id " + id));
    }

    @Transactional
    public ReservaArmario criar(ReservaArmario novaReserva) {
        validarPeriodo(novaReserva);
        validarConflito(novaReserva, null);

        novaReserva.setId(null);
        novaReserva.setAtivo(true);
        return repository.save(novaReserva);
    }

    @Transactional
    public ReservaArmario atualizar(Long id, ReservaArmario dados) {
        ReservaArmario reserva = buscarPorId(id);

        if (!Boolean.TRUE.equals(reserva.getAtivo())) {
            throw new RegraNegocioException("Reservas encerradas não podem ser editadas");
        }

        validarPeriodo(dados);
        // Ignora a própria reserva na checagem (senão ela conflitaria consigo mesma)
        validarConflito(dados, id);

        reserva.setNumeroArmario(dados.getNumeroArmario());
        reserva.setNomeAluno(dados.getNomeAluno());
        reserva.setDataInicio(dados.getDataInicio());
        reserva.setDataFim(dados.getDataFim());
        return repository.save(reserva);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarPorId(id));
    }

    @Transactional
    public ReservaArmario liberar(Long id) {
        ReservaArmario reserva = buscarPorId(id);
        reserva.setAtivo(false);
        return repository.save(reserva);
    }

    private void validarPeriodo(ReservaArmario reserva) {
        if (!reserva.getDataFim().isAfter(reserva.getDataInicio())) {
            throw new RegraNegocioException("A data de fim deve ser depois da data de início");
        }
    }

    private void validarConflito(ReservaArmario nova, Long idIgnorado) {
        List<ReservaArmario> existentes =
                repository.findByNumeroArmarioAndAtivoTrue(nova.getNumeroArmario());

        for (ReservaArmario reserva : existentes) {
            if (reserva.getId().equals(idIgnorado)) {
                continue;
            }
            if (nova.getDataInicio().isBefore(reserva.getDataFim())
                    && nova.getDataFim().isAfter(reserva.getDataInicio())) {
                throw new ConflitoReservaException(
                        "O armário já possui uma reserva ativa");
            }
        }
    }
}
