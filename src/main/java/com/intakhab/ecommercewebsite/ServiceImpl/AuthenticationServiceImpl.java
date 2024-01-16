package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.Email;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import com.intakhab.ecommercewebsite.Service.AuthenticationService;
import com.intakhab.ecommercewebsite.Service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${spring.mail.username}")
    private String sender;
    @Value("${server.port}")
    private int serverPort;
    //    @Value("${token.expiry.time}")
    private long tokenExpiryTime = 5 * 60 * 1000;

    private final UserRepo userRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepo userRepo, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUser(User newRegisterdUser) {
        newRegisterdUser.setRole("USER");
        newRegisterdUser.setToken("NULL");
        newRegisterdUser.setPassword(passwordEncoder.encode(newRegisterdUser.getPassword()));
        newRegisterdUser.setTokenExpiryTime(0);
        if (userRepo.findByUsernameOrEmailIdOrPhoneNumber(newRegisterdUser.getUsername(), newRegisterdUser.getEmailId(), newRegisterdUser.getPhoneNumber()) == null) {
            return userRepo.save(newRegisterdUser);
        }
        return null;
    }

    @Override
    public boolean sendResetPasswordMail(String emailUserPhone) {
        User user = userRepo.findByUsernameOrEmailIdOrPhoneNumber(emailUserPhone, emailUserPhone, emailUserPhone);
        if (user != null) {
            UUID uuid = UUID.randomUUID();
            String token = uuid.toString().replace("-", "");
            String mailMsg = generateResetLink(token);
            String subject = "Here's the link to reset your password";

            Email email = new Email(sender, user.getEmailId(), subject, mailMsg);

            user.setToken(token);
            user.setTokenExpiryTime((System.currentTimeMillis() + tokenExpiryTime));
            userRepo.save(user);
            return emailService.sendEmail(email);
        }
        return false;
    }

    @Override
    public String generateResetLink(String token) {

        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        assert localHost != null;
        String link = "http://" + localHost.getHostAddress() + ":" + serverPort + "/reset_password?token=" + token;

        String msg = "<html>" + "<body>" + "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>" + "<p>Click the link below to change your password:</p>" + "<p><a href=" + link + ">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, " + "or you have not made the request.</p>" + "</body>" + "</html>";
        return msg;
    }

    @Override
    public boolean validateUserToken(String token) {
        User user = userRepo.findByToken(token);
        return user != null && user.getTokenExpiryTime() > System.currentTimeMillis();
    }

    @Override
    public User findByToken(String token) {
        return userRepo.findByToken(token);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString().replace("-", "");
        user.setToken(token);
        userRepo.save(user);
    }
}
