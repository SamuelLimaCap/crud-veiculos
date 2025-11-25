package com.support.compracarros.services.impl;

import com.support.compracarros.config.UserDetailsImpl;
import com.support.compracarros.repositories.UserRespository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRespository userRespository;


    private com.support.compracarros.entities.User user;

    public UserDetailsServiceImpl(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user = userRespository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Collection<GrantedAuthority> authorities = user.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getPermission().name()))
                .collect(Collectors.toSet());

        return new UserDetailsImpl(user);
    }

}
