package com.support.compracarros.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "marcas")
@Table(name = "marcas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Marcas {

    @Id
    private String marca;

    @OneToMany(mappedBy = "marca")
    private List<Modelos> modelos;


}
