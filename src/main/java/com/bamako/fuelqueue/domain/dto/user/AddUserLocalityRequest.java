package com.bamako.fuelqueue.domain.dto.user;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class AddUserLocalityRequest {

    @NotNull
    private UUID localityId;

    private boolean primary;
}
