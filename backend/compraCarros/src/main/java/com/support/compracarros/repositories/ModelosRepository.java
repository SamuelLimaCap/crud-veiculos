package com.support.compracarros.repositories;

import com.support.compracarros.entities.Marcas;
import com.support.compracarros.entities.Modelos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelosRepository extends JpaRepository<Modelos, Long> {

    List<Modelos> findAllByMarca_Marca(String marca);

    String marca(Marcas marca);
}
