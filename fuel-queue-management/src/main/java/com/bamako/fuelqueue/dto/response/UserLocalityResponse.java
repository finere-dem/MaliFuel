package com.bamako.fuelqueue.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserLocalityResponse {
    private LocalityResponse locality;
    private boolean primary;
}
