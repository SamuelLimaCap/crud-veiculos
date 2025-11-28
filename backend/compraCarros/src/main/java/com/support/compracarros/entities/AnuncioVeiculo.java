package com.support.compracarros.entities;

import com.support.compracarros.models.AnuncioVeiculoState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity(name = "anuncioVeiculo")
@Table(name = "anuncioVeiculos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AnuncioVeiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @Column(unique = true, nullable = false)
    private String placa;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(length = 3, nullable = false)
    @Builder.Default
    private String moeda = "BRL";

    @Column(nullable = false, precision = 8, scale = 3)
    private BigDecimal kmRodados;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AnuncioVeiculoState estado = AnuncioVeiculoState.ABERTO;

    @Builder.Default
    private boolean deletado = false;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToMany(mappedBy = "anuncioVeiculo")
    private List<PedidoCompra> pedidoCompras;
}
