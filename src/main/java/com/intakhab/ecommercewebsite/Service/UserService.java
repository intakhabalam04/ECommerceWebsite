package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findByUserName(String username);

    User findByEmailId(String emailId);

    User findByPhoneNumber(String phoneNumber);

    List<User> getAllUsers();

    User findById(UUID id);
}
