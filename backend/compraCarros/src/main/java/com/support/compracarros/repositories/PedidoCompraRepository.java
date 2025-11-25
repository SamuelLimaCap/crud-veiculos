package com.support.compracarros.repositories;

import com.support.compracarros.dto.res.PedidoAnuncioRes;
import com.support.compracarros.entities.PedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    Optional<PedidoCompra> findByUser_IdAndAnuncioVeiculo_Id(Long userId, Long anuncioId);

    Page<PedidoCompra> findAll(Pageable pageable);


}
