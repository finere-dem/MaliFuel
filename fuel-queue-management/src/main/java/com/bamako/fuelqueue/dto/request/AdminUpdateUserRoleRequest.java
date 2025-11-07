package com.bamako.fuelqueue.dto.request;

import com.bamako.fuelqueue.domain.enumeration.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateUserRoleRequest {

    @NotNull
    private UserRole role;
}
