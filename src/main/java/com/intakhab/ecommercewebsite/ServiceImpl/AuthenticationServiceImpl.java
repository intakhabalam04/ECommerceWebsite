package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Enum.UserRole;
import com.intakhab.ecommercewebsite.Model.Email;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import com.intakhab.ecommercewebsite.Service.AuthenticationService;
import com.intakhab.ecommercewebsite.Service.EmailService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    // Injecting values from application.properties
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${server.port}")
    private int serverPort;
    @Value("${token.expiry.time}")
    private long tokenExpiryTime;

    // Injecting dependencies
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityConfig securityConfig;
    private final UserService userService;

    // Constructor to inject dependencies
    public AuthenticationServiceImpl(UserRepo userRepo, EmailService emailService, PasswordEncoder passwordEncoder, SecurityConfig securityConfig, UserService userService) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.securityConfig = securityConfig;
        this.userService = userService;
    }

    // Register a new user
    @Override
    public User registerNewUser(User newRegisteredUser) {
        // Set user details and save
        newRegisteredUser.setRole(UserRole.USER);
        newRegisteredUser.setToken("NULL");
        newRegisteredUser.setPassword(passwordEncoder.encode(newRegisteredUser.getPassword()));
        newRegisteredUser.setTokenExpiryTime(0);
        newRegisteredUser.setRegisterDate(LocalDate.now());

        // Check for existing user with the same username, email, or phone number
        if (userRepo.findByUsernameOrEmailIdOrPhoneNumber(newRegisteredUser.getUsername(), newRegisteredUser.getEmailId(), newRegisteredUser.getPhoneNumber()) != null) {
            throw new RuntimeException("User with the provided username, email, or phone number already exists");
        }

        return userRepo.save(newRegisteredUser);
    }

    // Send a reset password email
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

    // Generate a reset password link
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
        System.out.println(link);
        return msg;
    }

    // Validate user token
    @Override
    public boolean validateUserToken(String token) {
        User user = userRepo.findByToken(token);
        return user != null && user.getTokenExpiryTime() > System.currentTimeMillis();
    }

    // Find user by token
    @Override
    public User findByToken(String token) {
        return userRepo.findByToken(token);
    }

    // Update user password
    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString().replace("-", "");
        user.setToken(token);
        userRepo.save(user);
    }

    // Update user password with current user context
    @Override
    public boolean updatePassword(String currentPassword, String newPassword) {
        User currentUser = securityConfig.getCurrentUser();

        // Check if the provided current password matches the user's current password
        if (passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(currentUser);
            return true;
        }
        return false;
    }

    // Check username availability
    @Override
    public boolean checkAvailabilityByUsername(String username) {
        return userService.findByUserName(username) != null;
    }

    // Check email availability
    @Override
    public boolean checkAvailabilityByEmailId(String emailId) {
        return userService.findByEmailId(emailId) != null;
    }

    // Check phone number availability
    @Override
    public boolean checkAvailabilityByPhoneNumber(String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber) != null;
    }
}
