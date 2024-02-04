package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Model.User;

public interface AuthenticationService {
    User registerNewUser(User newRegisterdUser);
    boolean sendResetPasswordMail(String emailUserPhone);
    String generateResetLink(String token);
    boolean validateUserToken(String token);
    User findByToken(String token);
    void updatePassword(User user, String password);
    boolean updatePassword(String currentPassword, String newPassword);
    boolean checkAvailabilityByUsername(String username);
    boolean checkAvailabilityByEmailId(String emailId);
    boolean checkAvailabilityByPhoneNumber(String phoneNumber);
}
