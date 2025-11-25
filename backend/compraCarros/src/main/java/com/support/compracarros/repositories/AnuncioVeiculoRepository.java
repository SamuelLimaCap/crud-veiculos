package com.support.compracarros.repositories;

import com.support.compracarros.entities.AnuncioVeiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnuncioVeiculoRepository extends JpaRepository<AnuncioVeiculo, Long> {

     boolean existsByPlaca(String placa);

     Page<AnuncioVeiculo> findAllWhereDeletadoIsFalse(Pageable pageable);

}
