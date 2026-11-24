package com.escola.armarios.repository;

import com.escola.armarios.model.ReservaArmario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaArmarioRepository extends JpaRepository<ReservaArmario, Long> {

    List<ReservaArmario> findByNumeroArmarioAndAtivoTrue(String numeroArmario);
}
