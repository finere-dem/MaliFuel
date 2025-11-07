package com.bamako.fuelqueue.security;

import com.bamako.fuelqueue.entity.User;
import com.bamako.fuelqueue.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPrincipal implements UserDetails {

    private UUID id;
    private String phone;
    private String password;
    private Role role;

    public static UserPrincipal fromUser(User user) {
        return UserPrincipal.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    public UUID getUserId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
