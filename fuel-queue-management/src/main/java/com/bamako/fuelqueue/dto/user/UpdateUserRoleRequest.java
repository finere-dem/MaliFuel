package com.bamako.fuelqueue.dto.user;

import com.bamako.fuelqueue.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRoleRequest {

    @NotNull
    private Role role;
}
