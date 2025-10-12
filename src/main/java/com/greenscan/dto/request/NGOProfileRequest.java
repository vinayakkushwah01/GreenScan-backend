package com.greenscan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NGOProfileRequest {
    @NotNull(message = "User ID is required")
    private Long userId; // Link to the MainUser entity

    @NotBlank(message = "Organization name is required")
    private String organizationName;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Cause description is required")
    private String causeDescription;

    private String websiteUrl;

    // Bank details
    private String bankAccountNumber;
    private String bankIfscCode;
    private String bankAccountHolderName;
    private String bankName;

    // Impact details (optional)
    private Integer impactBeneficiaries;
    private String impactDescription;
}
