package com.support.compracarros.config;

import com.support.compracarros.controllers.AuthController;
import com.support.compracarros.dto.req.CreateUserRequest;
import com.support.compracarros.entities.UserPermission;
import com.support.compracarros.models.UserPermissionEnum;
import com.support.compracarros.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PermissionRepository repository;

    private final AuthController authController;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<UserPermissionEnum> notInsertedPermissions;
        if (repository.count() == 0) {
            notInsertedPermissions = List.of(UserPermissionEnum.values());
            notInsertedPermissions.forEach(p -> repository.save(UserPermission.builder().permission(p).build()));
        }
        if (repository.count() < 3) {
            List<UserPermissionEnum> userPermissions = repository.findAll().stream().map(UserPermission::getPermission).toList();
            List<UserPermissionEnum> localUserPermissions = List.of(UserPermissionEnum.values());

            notInsertedPermissions = localUserPermissions.stream().filter(p -> !userPermissions.contains(p)).toList();
            notInsertedPermissions.forEach(p -> repository.save(UserPermission.builder().permission(p).build()));
        }

    }
}
