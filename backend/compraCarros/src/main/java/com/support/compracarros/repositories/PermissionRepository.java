package com.support.compracarros.repositories;

import com.support.compracarros.entities.UserPermission;
import com.support.compracarros.models.UserPermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<UserPermission, Long> {
    UserPermission findByPermission(UserPermissionEnum permission);
}
