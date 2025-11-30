package com.support.compracarros.repositories;

import com.support.compracarros.entities.AnuncioVeiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioVeiculoRepository extends JpaRepository<AnuncioVeiculo, Long>, JpaSpecificationExecutor<AnuncioVeiculo> {

     boolean existsByPlacaAndDeletadoFalse(String placa);

     Page<AnuncioVeiculo> findByDeletadoFalse(Pageable pageable);

    List<AnuncioVeiculo> findByUser_IdAndDeletadoFalse(Long userId);


}
