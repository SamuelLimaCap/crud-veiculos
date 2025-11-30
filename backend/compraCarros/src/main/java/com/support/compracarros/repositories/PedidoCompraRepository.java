package com.support.compracarros.repositories;

import com.support.compracarros.entities.PedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    Optional<PedidoCompra> findByUser_IdAndAnuncioVeiculo_Id(Long userId, Long anuncioId);

    Page<PedidoCompra> findAll(Pageable pageable);

    List<PedidoCompra> findAllByDisabledFalseAndUser_Id(Long userId);

    List<PedidoCompra> findAllByDisabledFalseAndAnuncioVeiculo_Id(Long id);


}
