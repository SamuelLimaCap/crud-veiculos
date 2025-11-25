package com.support.compracarros.entities;

import com.support.compracarros.models.UserPermissionEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserPermissionEnum permission;
}
