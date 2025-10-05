package com.greenscan.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class ChangePasswordRequest {
    @NonNull
    private String oldPassword;
    @NonNull
    private String newPassword;


}
