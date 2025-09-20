package com.greenscan.entity;

import com.greenscan.enums.CartStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "cart_status_history", indexes = {
    @Index(name = "idx_cart_status_history_cart", columnList = "cart_id"),
    @Index(name = "idx_cart_status_history_created", columnList = "created_at")
})
@EqualsAndHashCode(callSuper = true)
public class CartStatusHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private CartStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    private CartStatus toStatus;

    @Column(name = "changed_by_user_id")
    private Long changedByUserId;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "system_generated")
    private Boolean systemGenerated = false;
}
