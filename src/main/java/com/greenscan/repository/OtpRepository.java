package com.greenscan.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.greenscan.entity.MainUser;
import com.greenscan.entity.Otp;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByMainUserAndOtp(MainUser mainUser, String otp);
    void deleteByMainUser(MainUser mainUser);
}