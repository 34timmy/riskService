package ru.mifi.authentication.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.mifi.authentication.model.JwtUser;
import ru.mifi.authentication.model.Role;
import ru.mifi.authentication.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles()),
                user.isEnabled(),
                user.getRegistered()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }
}
