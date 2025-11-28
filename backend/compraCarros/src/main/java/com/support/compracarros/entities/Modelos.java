package com.support.compracarros.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modelos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Modelos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca", referencedColumnName = "marca")
    @JsonIgnore
    private Marcas marca;

    private String label;

    @Column(name = "\"value\"")
    private int value;
}
