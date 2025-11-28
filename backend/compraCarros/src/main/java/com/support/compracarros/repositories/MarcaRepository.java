package com.support.compracarros.repositories;

import com.support.compracarros.entities.Marcas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marcas, String> {

}
