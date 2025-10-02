package com.greenscan.dto.response;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class DashboardStatsResponse {
     // For End Users
    private BigDecimal greenCoinsBalance;
    private Integer activeCartsCount;
    private Integer completedCartsCount;
    private BigDecimal totalWasteRecycled;
    
    // For Vendors
    private Integer pendingPickupsCount;
    private Integer todayPickupsCount;
    private BigDecimal todayRevenue;
    private BigDecimal pendingPayments;
    
    // For Admins
    private Integer totalUsers;
    private Integer totalVendors;
    private Integer totalCarts;
    private BigDecimal totalCoinsGenerated;
}
