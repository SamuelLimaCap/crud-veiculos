package com.support.compracarros.repositories;

import com.support.compracarros.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

}
