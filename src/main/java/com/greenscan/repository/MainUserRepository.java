package com.greenscan.repository;

import com.greenscan.entity.MainUser;
import com.greenscan.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MainUserRepository extends JpaRepository<MainUser, Long> {
    
    Optional<MainUser> findByEmailAndIsActiveTrue(String email);
    Optional<MainUser> findByIdAndIsActiveTrue(Long id);
    Optional<MainUser> findByMobileAndIsActiveTrue(String mobile);
    
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
    
    Page<MainUser> findByRoleAndIsActiveTrue(UserRole role, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.city IN :cities AND u.isActive = true")
    List<MainUser> findActiveUsersByRoleAndCities(@Param("role") UserRole role, @Param("cities") List<String> cities);
    
    @Query("SELECT u FROM User u WHERE u.role = 'VENDOR' AND u.city = :city AND u.isActive = true")
    List<MainUser> findActiveVendorsByCity(@Param("city") String city);
    
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :loginTime WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);
    
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = :attempts WHERE u.id = :userId")
    void updateFailedLoginAttempts(@Param("userId") Long userId, @Param("attempts") Integer attempts);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isActive = true")
    long countByRoleAndIsActiveTrue(@Param("role") UserRole role);
}