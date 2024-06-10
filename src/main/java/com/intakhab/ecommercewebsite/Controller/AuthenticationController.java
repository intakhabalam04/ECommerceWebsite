package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import com.intakhab.ecommercewebsite.Service.AuthenticationService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthenticationController {

    @Value("${loginView}")
    private String loginView;

    @Value("${registerView}")
    private String registerView;

    @Value("${resetFormView}")
    private String resetFormView;

    @Value("${invalidView}")
    private String invalidView;

    @Value("${successView}")
    private String successView;

    private final AuthenticationService authenticationService;
    private final SecurityConfig securityConfig;
    private final UserRepo userRepo;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, SecurityConfig securityConfig, UserRepo userRepo, UserService userService) {
        this.authenticationService = authenticationService;
        this.securityConfig = securityConfig;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        RedirectView rd = new RedirectView("/user/product");
        return new ModelAndView(rd);
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        Map<String, Object> model = new HashMap<>();
        model.put("LoginUser", new User());
        model.put("ForgotUser", new User());
        return new ModelAndView(loginView, model);
    }

    @PostMapping("/login")
    public ModelAndView submitLoginPage(@ModelAttribute("LoginUser") User loginUser) {
        return new ModelAndView(successView);
    }

    @GetMapping("/register")
    public ModelAndView getRegistrationPage() {
        Map<String, Object> model = new HashMap<>();
        model.put("NewUser", new User());
        return new ModelAndView(registerView, model);
    }

    @PostMapping("/register")
    public ModelAndView submitRegistrationForm(@ModelAttribute("NewUser") User newRegisteredUser) {
        User user = authenticationService.registerNewUser(newRegisteredUser);
        Map<String, Object> model = new HashMap<>();
        if (user == null) {
            return new ModelAndView(registerView, model);
        }
        model.put("success", "Your registration is successfully completed");
        return new ModelAndView(registerView, model);
    }

    @GetMapping("/forgot")
    @ResponseBody
    public boolean submitForgotPage(@RequestParam("userId") String userId) {
        return authenticationService.sendResetPasswordMail(userId);
    }

    @GetMapping("/reset_password")
    public ModelAndView getResetPage(@RequestParam String token) {

        User user = authenticationService.findByToken(token);
        Map<String, Object> model = new HashMap<>();

        if (user == null || !authenticationService.validateUserToken(token)) {
            return new ModelAndView(invalidView);
        } else {
            model.put("forgot", user);
            return new ModelAndView(resetFormView, model);
        }
    }

    @PostMapping("/reset_password")
    public ModelAndView submitResetPage(@ModelAttribute("forgot") User user) {
        if (authenticationService.validateUserToken(user.getToken())) {
            authenticationService.updatePassword(authenticationService.findByToken(user.getToken()), user.getPassword());
            return new ModelAndView(successView);
        } else {
            return new ModelAndView(invalidView);
        }
    }

    @GetMapping("/change-password")
    @ResponseBody
    public boolean changePassword(@RequestParam("cp") String currentPassword, @RequestParam("np") String newPassword) {
        return authenticationService.updatePassword(currentPassword, newPassword);

    }

    @GetMapping("checkAvailability")
    @ResponseBody
    public boolean checkAvailability(@RequestParam("userId") String userId, @RequestParam("type") String type) {
        return switch (type) {
            case "username" -> authenticationService.checkAvailabilityByUsername(userId);
            case "emailId" -> authenticationService.checkAvailabilityByEmailId(userId);
            case "phoneNumber" -> authenticationService.checkAvailabilityByPhoneNumber(userId);
            default -> false;
        };
    }

    @PostMapping("/upload-profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            User user = securityConfig.getCurrentUser();
            user.setProfileImg(file.getBytes());
            userRepo.save(user);

            return new ResponseEntity<>("{\"message\": \"Profile image uploaded successfully!\"}", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("{\"error\": \"Error uploading profile image\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/user/image")
    public ResponseEntity<byte[]> getUserProfileImage() throws IOException {


        User currentUser = securityConfig.getCurrentUser();

        if (currentUser.getProfileImg() == null) {
            char firstLetter = currentUser.getName().charAt(0);
            Resource resource = new ClassPathResource("static/Images/dp_images/" + firstLetter + ".png");

            byte[] imageBytes = Files.readAllBytes(Path.of(resource.getURI()));

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // Adjust the content type based on your image type
                    .body(imageBytes);

        } else {
            byte[] imageData = userService.getUserImageById(currentUser.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        }
    }

    @GetMapping("/removeProfileImage")
    @ResponseBody
    public Object removeProfileImage() {
        User user = securityConfig.getCurrentUser();
        user.setProfileImg(null);
        userRepo.save(user);
        return true;
    }
}
