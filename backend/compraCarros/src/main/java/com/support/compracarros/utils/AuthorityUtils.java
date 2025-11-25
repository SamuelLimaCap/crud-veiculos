package com.support.compracarros.utils;

import com.support.compracarros.entities.UserPermission;
import com.support.compracarros.models.UserPermissionEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorityUtils {
    public static List<? extends GrantedAuthority> permissionToGrantedAuthority(List<UserPermission> permissions) {

       return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getPermission().name()))
                .collect(Collectors.toList());
    }

    public static List<UserPermissionEnum> grantedAuthorityToUserPermission(List<GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth)
                .map(UserPermissionEnum::valueOf)
                .collect(Collectors.toList());
    }
}
