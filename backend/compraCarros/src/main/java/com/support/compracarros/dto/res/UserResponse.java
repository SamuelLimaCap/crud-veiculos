package com.support.compracarros.dto.res;

import com.support.compracarros.models.UserPermissionEnum;

import java.util.List;

public record UserResponse(
        String fullName,
        String email,
        List<UserPermissionEnum> permissions

) {
}
