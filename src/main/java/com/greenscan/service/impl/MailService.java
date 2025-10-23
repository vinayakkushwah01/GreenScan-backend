package com.greenscan.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    private static final String LOGO_URL = "https://yvtvwetbpdcjzobgjhsp.supabase.co/storage/v1/object/public/GreenScan/1000130550-removebg-preview.png";
    private static final String FROM_EMAIL = "service.greenscan@gmail.com";

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // -------------------------------------------------------
    // 🔹 Common HTML Email Builder
    // -------------------------------------------------------
    private String buildHtmlEmail(String title, String mainMessage, String highlightBox) {
        return "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'><style>"
                + "body { font-family: Arial, sans-serif; background-color: #f8f8f8; text-align:center; padding:20px; }"
                + ".card { max-width:500px; margin:auto; background:white; border-radius:12px; padding:25px; "
                + "box-shadow:0 4px 15px rgba(0,0,0,0.1); border-top:5px solid #28a745; }"
                + ".logo { width:120px; margin-bottom:15px; }"
                + "h2 { color:#28a745; }"
                + ".msg { color:#333; font-size:15px; line-height:1.6; }"
                + ".highlight { font-size:18px; font-weight:700; color:#ffffff; background-color:#28a745; padding:10px 20px; border-radius:6px; display:inline-block; margin:15px 0; }"
                + ".footer { font-size:12px; color:#888; margin-top:30px; border-top:1px dashed #ccc; padding-top:10px; }"
                + "</style></head><body>"
                + "<div class='card'>"
                + "<img src='" + LOGO_URL + "' class='logo' alt='GreenScan Logo'/>"
                + "<h2>" + title + "</h2>"
                + "<p class='msg'>" + mainMessage + "</p>"
                + (highlightBox != null ? "<div class='highlight'>" + highlightBox + "</div>" : "")
                + "<div class='footer'>&copy; 2025 GreenScan. All rights reserved.</div>"
                + "</div></body></html>";
    }

    // -------------------------------------------------------
    // 🔹 Common Method to Send Mail
    // -------------------------------------------------------
    private void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom(FROM_EMAIL);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to prepare or send email: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // 🔹  END USer Notification Methods
    // -------------------------------------------------------


    public void notifyUserPickupScheduled(String userEmail, String vendorName, String pickupTime) {
        String title = "Pickup Scheduled!";
        String msg = "Dear user, your pickup has been scheduled by vendor <b>" + vendorName + "</b> at <b>" + pickupTime + "</b>.";
        sendHtmlMail(userEmail, title, buildHtmlEmail(title, msg, pickupTime));
    }

    public void notifyUserAssistantArrived(String userEmail, String assistantName) {
        String title = "Assistant Arrived!";
        String msg = "Your pickup assistant <b>" + assistantName + "</b> has arrived. Please prepare your items for collection.";
        sendHtmlMail(userEmail, title, buildHtmlEmail(title, msg, assistantName));
    }

    public void notifyUserCoinsCredited(String userEmail, BigDecimal coins) {
        String title = "Coins Credited!";
        String msg = "Congratulations! You’ve received <b>" + coins + "</b> GreenScan coins for your recent recycling pickup.";
        sendHtmlMail(userEmail, title, buildHtmlEmail(title, msg, coins + " Coins"));
    }

    public void notifyUserActualWeightAndCoins(String userEmail, BigDecimal actualWeight, BigDecimal actualCoins) {
        String title = "Actual Weight & Coins Updated";
        String msg = "The actual weight of your recyclables is <b>" + actualWeight + " kg</b>, and you have earned <b>" + actualCoins + " coins</b>.";
        sendHtmlMail(userEmail, title, buildHtmlEmail(title, msg, actualWeight + " kg / " + actualCoins + " Coins"));
    }

    public void notifyUserCartRejectedByVendor(String userEmail, String vendorName, String reason) {
        String title = "Cart Rejected by Vendor";
        String msg = "Dear user, your cart request has been <b>rejected</b> by vendor <b>" + vendorName + "</b>.";
        
        // Add reason if provided
        if (reason != null && !reason.isBlank()) {
            msg += "<br><br><b>Reason:</b> " + reason;
        } else {
            msg += "<br><br><b>Reason:</b> No specific reason provided.";
        }

        msg += "<br><br>You can review your cart details and try again with another vendor.";

        sendHtmlMail(
            userEmail,
            title,
            buildHtmlEmail(title, msg, "Vendor: " + vendorName)
        );
    }

    public void notifyUserCartStatusChange(String userEmail, String cartId, String newStatus, String notes) {
    String title = "Cart Status Updated!";
    
    String msg = "Dear user, the status of your cart (ID: <b>" + cartId + "</b>) has been updated to <b>" 
            + newStatus + "</b>.";

    if (notes != null && !notes.isBlank()) {
        msg += "<br><br><b>Notes:</b> " + notes;
    }

    msg += "<br><br>Thank you for using <b>GreenScan</b>. You can check your dashboard for more details.";

    sendHtmlMail(
        userEmail,
        title,
        buildHtmlEmail(title, msg, "Status: " + newStatus)
    );
}

    public void notifyUserCartCompleted(String userEmail, String cartId) {
    String title = "Pickup Completed Successfully!";
    String msg = "Dear user, your recycling cart (ID: <b>" + cartId + "</b>) has been successfully <b>completed</b>."
               + "<br><br>Thank you for contributing towards a cleaner and greener environment with <b>GreenScan</b>!";

    sendHtmlMail(
        userEmail,
        title,
        buildHtmlEmail(title, msg, "Status: Completed ✅")
    );
}

// -------------------------------------------------------
// 🔹 Vendor Profile Notification Mails (Admin Actions)
// -------------------------------------------------------

public void notifyVendorProfileApproved(String vendorEmail, String vendorName) {
    String title = "🎉 Vendor Profile Approved!";
    String msg = "Dear <b>" + vendorName + "</b>, your vendor profile has been <b>approved</b> by our admin team."
            + "<br><br>You can now start accepting recycling pickups and managing your dashboard."
            + "<br><br>Welcome to <b>GreenScan Vendor Network</b>!";
    sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, "Status: Approved ✅"));
}

public void notifyVendorProfileRejected(String vendorEmail, String vendorName, String reason) {
    String title = "❌ Vendor Profile Rejected";
    String msg = "Dear <b>" + vendorName + "</b>, unfortunately your vendor profile has been <b>rejected</b> by our admin team.";
    
    if (reason != null && !reason.isBlank()) {
        msg += "<br><br><b>Reason:</b> " + reason;
    } else {
        msg += "<br><br><b>Reason:</b> Not specified.";
    }

    msg += "<br><br>You can review and update your submitted details, then reapply for approval.";

    sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, "Status: Rejected ❌"));
}

public void notifyVendorProfileBlocked(String vendorEmail, String vendorName, String reason) {
    String title = "🚫 Vendor Account Blocked";
    String msg = "Dear <b>" + vendorName + "</b>, your vendor profile has been <b>blocked</b> due to policy or activity concerns.";

    if (reason != null && !reason.isBlank()) {
        msg += "<br><br><b>Reason:</b> " + reason;
    } else {
        msg += "<br><br><b>Reason:</b> Not provided.";
    }

    msg += "<br><br>If you believe this was a mistake, please contact GreenScan support.";

    sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, "Status: Blocked 🚫"));
}

public void notifyVendorProfileUnblocked(String vendorEmail, String vendorName, String reason) {
    String title = "✅ Vendor Account Unblocked";
    String msg = "Good news <b>" + vendorName + "</b>! Your vendor profile has been <b>unblocked</b> and reactivated."
            + "<br><br><b>Reason:</b> " + reason
            + "<br><br>You can now continue your operations normally on <b>GreenScan</b>.";

    sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, "Status: Active ✅"));
}

    public void notifyVendorPickupRequest(String vendorEmail, String cartId, String userName) {
        String title = "New Pickup Request!";
        String msg = "Dear Vendor, you have received a new pickup request from <b>" + userName + "</b> for cart ID <b>" + cartId + "</b>. Please confirm it at your earliest convenience.";
        sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, "Cart ID: " + cartId));
    }

    public void notifyVendorAssignAssistant(String vendorEmail, String assistantName, String cartId) {
        String title = "Assistant Assigned!";
        String msg = "You have assigned <b>" + assistantName + "</b> to pickup cart ID <b>" + cartId + "</b>.";
        sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, assistantName));
    }

    public void notifyVendorAssistantReached(String vendorEmail, String assistantName, String userAddress) {
        String title = "Assistant Reached Pickup Location!";
        String msg = "Your assistant <b>" + assistantName + "</b> has reached the user’s location: <b>" + userAddress + "</b>.";
        sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, assistantName));
    }

    public void notifyVendorAssistantAtShop(String vendorEmail, String assistantName) {
        String title = "Assistant Arrived at Shop!";
        String msg = "Your assistant <b>" + assistantName + "</b> has arrived back at your shop with the collected items.";
        sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, assistantName));
    }

    public void notifyVendorManualChangeRequest(String vendorEmail, String cartId) {
        String title = "Manual Change Requested";
        String msg = "Please review and confirm the manual changes made to cart ID <b>" + cartId + "</b> before final approval.";
        sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, "Cart ID: " + cartId));
    }

    public void notifyVendorAssignedToCart(String vendorEmail, String cartId) {
        String title = "New Cart Assigned!";
        String msg = "A new cart (ID: <b>" + cartId + "</b>) has been assigned to you. Please check your dashboard for details.";
        sendHtmlMail(vendorEmail, title, buildHtmlEmail(title, msg, "Cart ID: " + cartId));
    }

}
