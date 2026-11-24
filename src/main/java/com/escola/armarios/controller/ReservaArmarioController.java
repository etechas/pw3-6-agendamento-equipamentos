package com.escola.armarios.controller;

import com.escola.armarios.model.ReservaArmario;
import com.escola.armarios.service.ReservaArmarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/armarios")
@RequiredArgsConstructor
public class ReservaArmarioController {

    private final ReservaArmarioService service;

    @GetMapping
    public List<ReservaArmario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ReservaArmario buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ReservaArmario> criar(@Valid @RequestBody ReservaArmario reserva) {
        ReservaArmario criada = service.criar(reserva);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public ReservaArmario atualizar(@PathVariable Long id, @Valid @RequestBody ReservaArmario reserva) {
        return service.atualizar(id, reserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/liberar")
    public ReservaArmario liberar(@PathVariable Long id) {
        return service.liberar(id);
    }
}
