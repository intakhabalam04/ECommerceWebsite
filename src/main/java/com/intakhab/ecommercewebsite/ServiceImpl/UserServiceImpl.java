package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User findByUserName(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User findByEmailId(String emailId) {
        return userRepo.findByEmailId(emailId);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userLists = userRepo.findAll(Sort.by("name").ascending());
        List<User> roleUserLists = new ArrayList<>();
        for (User user : userLists) {
            if (user.getRole().equals("USER")) {
                roleUserLists.add(user);
            }
        }
        return roleUserLists;
    }

    @Override
    public User findById(UUID id) {
        if (userRepo.findById(id).isPresent()) {
            return userRepo.findById(id).get();
        }
        return null;
    }

}
