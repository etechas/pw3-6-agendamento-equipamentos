package com.enzoHeitor.agendamento.equipamentos.service;

import com.enzoHeitor.agendamento.equipamentos.entity.ReservaArmario;
import com.enzoHeitor.agendamento.equipamentos.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository repository;

    public ReservaService(ReservaRepository repository) {
        this.repository = repository;
    }

    public List<ReservaArmario> listar() {
        return repository.findAll();
    }

    public List<ReservaArmario> listarAtivas() {
        return repository.findByAtivoTrue();
    }

    public ReservaArmario buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada"));
    }

    public ReservaArmario criar(ReservaArmario reserva) {
        reserva.setId(null);
        reserva.setAtivo(true);
        validar(reserva, -1, true);
        return repository.save(reserva);
    }

    public ReservaArmario atualizar(Integer id, ReservaArmario dados) {
        ReservaArmario existente = buscarPorId(id);
        // só reservas ativas ocupam o armário; reserva já liberada pode ser editada sem checar conflito
        validar(dados, id, Boolean.TRUE.equals(existente.getAtivo()));

        existente.setNumeroArmario(dados.getNumeroArmario());
        existente.setNomeAluno(dados.getNomeAluno());
        existente.setDataInicio(dados.getDataInicio());
        existente.setDataFim(dados.getDataFim());
        return repository.save(existente);
    }

    public void excluir(Integer id) {
        repository.delete(buscarPorId(id));
    }

    public ReservaArmario liberar(Integer id) {
        ReservaArmario reserva = buscarPorId(id);
        if (!Boolean.TRUE.equals(reserva.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A reserva já está finalizada");
        }
        reserva.setAtivo(false);
        return repository.save(reserva);
    }

    private void validar(ReservaArmario r, Integer idIgnorado, boolean verificarConflito) {
        if (r.getNumeroArmario() == null || r.getNumeroArmario() <= 0) {
            throw erro("Número do armário é obrigatório e deve ser positivo");
        }
        if (r.getNomeAluno() == null || r.getNomeAluno().isBlank()) {
            throw erro("Nome do aluno é obrigatório");
        }
        if (r.getDataInicio() == null || r.getDataFim() == null) {
            throw erro("Data de início e data de fim são obrigatórias");
        }
        if (r.getDataFim().isBefore(r.getDataInicio())) {
            throw erro("A data de fim não pode ser anterior à data de início");
        }
        boolean conflito = verificarConflito && repository.existeConflito(
                r.getNumeroArmario(), r.getDataInicio(), r.getDataFim(), idIgnorado);
        if (conflito) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe uma reserva ativa para este armário no período informado");
        }
    }

    private ResponseStatusException erro(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}
