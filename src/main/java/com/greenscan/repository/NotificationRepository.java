package com.greenscan.repository;

import com.greenscan.entity.Notification;
import com.greenscan.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    Page<Notification> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<Notification> findByUserIdAndIsReadFalseAndIsActiveTrueOrderByCreatedAtDesc(Long userId);
    
    long countByUserIdAndIsReadFalseAndIsActiveTrue(Long userId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP WHERE n.user.id = :userId AND n.isRead = false")
    void markAllAsReadByUserId(@Param("userId") Long userId);
    
    @Query("SELECT n FROM Notification n WHERE n.notificationType = :type AND n.isActive = true ORDER BY n.createdAt DESC")
    List<Notification> findByNotificationType(@Param("type") NotificationType type);
}