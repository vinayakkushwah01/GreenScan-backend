package com.greenscan.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class UpdateLocationRequest {
   @NonNull
    private Double latitude;
    @NonNull
    private Double longitude;
}
