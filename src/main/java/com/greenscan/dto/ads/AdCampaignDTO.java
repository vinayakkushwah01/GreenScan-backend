package com.greenscan.dto.ads;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.AdCampaignStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdCampaignDTO extends BaseDTO {
    private Long adsCompanyId;
    private String campaignName;
    private String description;
    private AdCampaignStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalBudget;
    private BigDecimal dailyBudget;
    private BigDecimal spentAmount;
    private Integer targetAgeMin;
    private Integer targetAgeMax;
    private List<String> targetCities;
    private List<String> targetStates;
    private List<String> targetInterests;
    private String mediaUrl;
    private String mediaType;
    private String redirectUrl;
    private Long totalImpressions;
    private Long totalClicks;
    private BigDecimal ctr;
}