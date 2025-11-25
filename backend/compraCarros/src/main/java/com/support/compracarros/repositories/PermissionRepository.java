package com.support.compracarros.repositories;

import com.support.compracarros.entities.UserPermission;
import com.support.compracarros.models.UserPermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<UserPermission, Long> {
    UserPermission findByPermission(UserPermissionEnum permission);
}
