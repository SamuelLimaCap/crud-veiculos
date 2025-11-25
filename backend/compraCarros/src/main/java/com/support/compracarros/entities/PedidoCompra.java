package com.support.compracarros.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "pedido_compra")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anuncio_id", referencedColumnName = "id")
    private AnuncioVeiculo anuncioVeiculo;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    @Lob
    private String message;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean disabled = false;

}
