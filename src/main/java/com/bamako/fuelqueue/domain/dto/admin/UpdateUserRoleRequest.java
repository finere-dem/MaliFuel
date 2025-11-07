package com.bamako.fuelqueue.domain.dto.admin;

import com.bamako.fuelqueue.domain.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    @NotNull
    private UserRole role;
}
