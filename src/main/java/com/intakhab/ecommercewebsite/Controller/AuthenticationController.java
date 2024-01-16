package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.AuthenticationService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        RedirectView rd = new RedirectView("/user/product");
        return new ModelAndView(rd);
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        String viewName = "Security/login";
        Map<String, Object> model = new HashMap<>();
        model.put("LoginUser", new User());
        model.put("ForgotUser", new User());
        return new ModelAndView(viewName, model);
    }

    @PostMapping("/login")
    public ModelAndView submitLoginPage(@ModelAttribute("LoginUser") User loginUser) {
        System.out.println(loginUser.toString() + "Inside the login page");
        return new ModelAndView("Security/success");
    }

    @GetMapping("/register")
    public ModelAndView getRegistrationPage() {
        String viewName = "Security/register";
        Map<String, Object> model = new HashMap<>();
        model.put("NewUser", new User());
        return new ModelAndView(viewName, model);
    }

    @PostMapping("/register")
    public ModelAndView submitRegistrationPage(@ModelAttribute("NewUser") User newRegisterdUser) {
        User user = authenticationService.registerNewUser(newRegisterdUser);
        String viewName = "Security/register";
        Map<String, Object> model = new HashMap<>();
        if (user == null) {
            if (userService.findByUserName(newRegisterdUser.getUsername()) != null)
                model.put("duplicateUserName", "Username already exist");
            if (userService.findByEmailId(newRegisterdUser.getEmailId()) != null)
                model.put("duplicateEmailId", "EmailId already exist");
            if (userService.findByPhoneNumber(newRegisterdUser.getPhoneNumber()) != null)
                model.put("duplicatePhoneNumber", "Phone number already exist");
            return new ModelAndView(viewName, model);
        }
        model.put("success", "Your registration is successfully completed");

        return new ModelAndView(viewName, model);
    }

    @PostMapping("/forgot")
    public ModelAndView submitForgotPage(@ModelAttribute("ForgotUser") User forgotUser) {
        System.out.println("Inside the forgot page");
        Map<String, Object> model = new HashMap<>();
        if (authenticationService.sendResetPasswordMail(forgotUser.getEmailUserPhone())) {
            model.put("message", "We have sent a reset password link to your email.Please check");
            System.out.println("UserFound");
        } else {
            model.put("error", "User Not Found!!");
            System.out.println("UserNotFound");
        }
        return new ModelAndView(new RedirectView("/login"));
    }

    @GetMapping("/reset_password")
    public ModelAndView getResetPage(@RequestParam String token) {

        User user = authenticationService.findByToken(token);
        String viewName;
        Map<String, Object> model = new HashMap<>();

        if (user == null || !authenticationService.validateUserToken(token)) {
            return new ModelAndView("Security/invalid");
        } else {
            viewName = "Security/reset-form";
            model.put("forgot", user);
            return new ModelAndView(viewName, model);
        }
    }

    @PostMapping("/reset_password")
    public ModelAndView submitResetPage(@ModelAttribute("forgot") User user) {
        if (authenticationService.validateUserToken(user.getToken())) {
            authenticationService.updatePassword(authenticationService.findByToken(user.getToken()), user.getPassword());
            return new ModelAndView("Security/success");
        } else {
            return new ModelAndView("Security/invalid");
        }
    }
}
