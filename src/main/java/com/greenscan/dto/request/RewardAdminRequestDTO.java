package com.greenscan.dto.request;

import com.greenscan.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardAdminRequestDTO {

    // ====== Basic Reward Info ======
    private String title;
    private String description;
    private String category; // e.g. VOUCHER, DISCOUNT, PRODUCT, CASHBACK

    private BigDecimal coinsRequired;
    private BigDecimal monetaryValue;

    // ====== Image Uploads ======
    /** Image for the reward (uploaded as file, processed in service layer) */
    private File rewardImageFile;

    /** Partner logo file (uploaded separately, processed in service layer) */
    private File partnerLogoFile;

    /** URLs after upload — filled automatically by the service layer */
    private String imageUrl;
    private String partnerLogoUrl;

    // ====== Additional Info ======
    private String termsConditions;
    private String redemptionInstructions;

    // ====== Admin & Approval ======
    private ApprovalStatus approvalStatus;
    private Long createdByAdminId;

    // ====== Availability ======
    private LocalDateTime availableFrom;
    private LocalDateTime availableUntil;

    private Integer totalQuantity;
    private Integer maxPerUser;

    // ====== Partner Info ======
    private String partnerName;

    // ====== Status ======
    private Boolean isActive;

    // 🔹 Constructor for editing an existing reward (populate fields from entity)
    public RewardAdminRequestDTO(com.greenscan.entity.Reward reward) {
        this.title = reward.getTitle();
        this.description = reward.getDescription();
        this.category = reward.getCategory();
        this.coinsRequired = reward.getCoinsRequired();
        this.monetaryValue = reward.getMonetaryValue();

        // Images — file uploads not included in edit prefill
        this.imageUrl = reward.getImageUrl();
        this.partnerLogoUrl = reward.getPartnerLogoUrl();

        this.termsConditions = reward.getTermsConditions();
        this.redemptionInstructions = reward.getRedemptionInstructions();

        this.approvalStatus = reward.getApprovalStatus();
        this.createdByAdminId = reward.getCreatedByAdminId();

        this.availableFrom = reward.getAvailableFrom();
        this.availableUntil = reward.getAvailableUntil();
        this.totalQuantity = reward.getTotalQuantity();
        this.maxPerUser = reward.getMaxPerUser();

        this.partnerName = reward.getPartnerName();
        this.isActive = reward.getIsActive();
    }
}
