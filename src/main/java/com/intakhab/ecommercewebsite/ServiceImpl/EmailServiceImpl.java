package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.Email;
import com.intakhab.ecommercewebsite.Service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Method to send an email using JavaMailSender
    @Override
    public boolean sendEmail(Email email) {
        try {
            // Create a MimeMessage using the injected JavaMailSender
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Use MimeMessageHelper for easy handling of MIME messages
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Set the recipient, subject, and text of the email
            mimeMessageHelper.setTo(email.getTo());
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(email.getMsgBody(), true);

            // Send the email using the JavaMailSender
            javaMailSender.send(mimeMessage);

            return true; // Email sent successfully

        } catch (Exception e) {
            // Handle any exceptions that occur during the email sending process
            System.out.println("Error in sending email" + e.getMessage());
        }
        return false; // Email sending failed
    }
}
