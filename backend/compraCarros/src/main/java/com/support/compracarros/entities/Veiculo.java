package com.support.compracarros.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "veiculo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer ano;

    private String combustivel;

    @Column(nullable = false)
    private String cor;

    @Column(nullable = false)
    @Builder.Default
    private String tipo = "carro";

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "veiculo")
    private List<AnuncioVeiculo> anuncios;


    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}
