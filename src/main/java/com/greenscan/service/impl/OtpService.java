package com.greenscan.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.greenscan.entity.MainUser;
import com.greenscan.entity.Otp;
import com.greenscan.exception.custom.InvalidOtpException;
import com.greenscan.exception.custom.MailSendException;
import com.greenscan.repository.OtpRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    public OtpService(OtpRepository otpRepository, JavaMailSender mailSender) {
        this.otpRepository = otpRepository;
        this.mailSender = mailSender;
    }

    // Generate OTP and send email
    public void generateAndSendOtp(MainUser user) {
        Random random = new Random();
        int otpInt = 100000 + random.nextInt(900000);
        String otpCode = String.valueOf(otpInt);

        // Remove existing OTPs
        otpRepository.deleteByMainUser(user);

        // Save new OTP
        Otp otp = new Otp(otpCode, LocalDateTime.now().plusMinutes(5), user);
        otpRepository.save(otp);

        // // Send email
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setTo(user.getEmail());
        // message.setSubject("Your OTP for Password Reset");
        // message.setText("Use this OTP to reset your password: " + otpCode + "\nIt expires in 5 minutes.");
        // message.setFrom("service.greenscan@gmail.com");

        // mailSender.send(message);

        // 1. Define the HTML content with the OTP code interpolated
        String htmlMsg = "<!DOCTYPE html>"
        + "<html>"
        + "<head>"
        + "<meta charset=\"UTF-8\">"
        + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
        + "<style>"
        + "  /* Global Container and Font */"
        + "  .container { font-family: Arial, sans-serif; padding: 20px; background-color: #f8f8f8; background-image: url('https://yvtvwetbpdcjzobgjhsp.supabase.co/storage/v1/object/public/GreenScan/1000130550-removebg-preview.png'); background-repeat: no-repeat; background-position: center center; background-size: 80%; text-align: center; line-height: 1.6; color: #333333; }"
        + "  .card { max-width: 500px; margin: 20px auto; background: #ffffff; padding: 30px; border-radius: 12px; text-align: center; box-shadow: 0 4px 15px rgba(0,0,0,0.1); border-top: 5px solid #28a745; }"
        + "  .logo { width: 120px; height: 120px; margin-bottom: 15px; }"
        + "  h2 { color: #28a745; margin-top: 0; font-size: 24px; }"
        + "  .tagline { font-size: 14px; color: #007bff; font-weight: bold; margin-bottom: 25px; }"
        + "  .otp-box { font-size: 32px; font-weight: 900; letter-spacing: 6px; color: #ffffff; background-color: #28a745; padding: 15px 30px; border-radius: 8px; display: inline-block; margin: 25px 0; text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1); }"
        + "  .platform-info { font-size: 14px; color: #6c757d; margin-top: 15px; font-style: italic; }"
        + "  .footer { font-size: 12px; color: #888888; margin-top: 30px; padding-top: 15px; border-top: 1px dashed #cccccc; }"
        + "  .footer a { color: #007bff; text-decoration: none; margin: 0 5px; }"
        + "</style>"
        + "</head>"
        + "<body>"
        + "<div class='container'>"
        + "  <div class='card'>"
        + "    <img src='https://yvtvwetbpdcjzobgjhsp.supabase.co/storage/v1/object/public/GreenScan/1000130550-removebg-preview.png' alt='GreenScan Logo' class='logo' width=\"120\" height=\"120\"/>"
        + "    <p class=\"tagline\">Reduce, Reuse, and Recycle!</p>"
        + "    <h2>Password Reset OTP</h2>"
        + "    <p>Use the <strong>One-Time Password (OTP)</strong> below to reset your password. For security, this code will expire in <strong>5 minutes</strong>.</p>"
        + "    <div class='otp-box'>" + otpCode + "</div>"
        + "    <p>If you did not request a password reset, please safely ignore this email.</p>"
        + "    <p class=\"platform-info\">"
        + "        Scan and recycle for the future! "
        + "        <br>A recycle product sell platform where you can gain rewards."
        + "    </p>"
        + "    <div class='footer'>"
        + "        &copy; 2025 GreenScan. All rights reserved."
        + "        <br>"
        + "        <a href=\"#\">Unsubscribe</a> | <a href=\"#\">Privacy Policy</a>"
        + "    </div>"
        + "  </div>"
        + "</div>"
        + "</body>"
        + "</html>";

        // 2. Switch to MimeMessage to send HTML
            try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            
            // The constructor below throws the exception
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); 

            helper.setTo(user.getEmail());
            helper.setSubject("Your OTP for Password Reset");
            helper.setFrom("service.greenscan@gmail.com");
            helper.setText(htmlMsg, true);

            mailSender.send(mimeMessage);
            
        } catch (MessagingException e) {
            throw new MailSendException("Failed to prepare or send password reset email.");
        }
        catch (Exception e) {
        // Catch other potential errors (like IO errors during setup)
         throw new MailSendException("An unexpected error occurred during email creation/sending.");
        }
    }

    // Validate OTP
   public boolean validateOtp(MainUser user, String otpCode) {
        Otp otp = otpRepository.findByMainUserAndOtp(user, otpCode)
                .orElseThrow(() -> new InvalidOtpException("Invalid OTP"));

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            throw new InvalidOtpException("OTP expired");
        }

        otpRepository.delete(otp); // OTP used
        return true;
    }
}