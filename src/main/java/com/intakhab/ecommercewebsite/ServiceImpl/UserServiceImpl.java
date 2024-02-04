package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    // Constructor to inject UserRepo dependency
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Find user by username
    @Override
    public User findByUserName(String username) {
        return userRepo.findByUsername(username);
    }

    // Find user by email ID
    @Override
    public User findByEmailId(String emailId) {
        return userRepo.findByEmailId(emailId);
    }

    // Find user by phone number
    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepo.findByPhoneNumber(phoneNumber);
    }

    // Get all users with role "USER", sorted by name in ascending order
    @Override
    public List<User> getAllUsers() {
        List<User> userLists = userRepo.findAll(Sort.by("name").ascending());
        return userLists.stream()
                .filter(user -> user.getRole().equals("USER"))
                .collect(Collectors.toList());
    }

    // Find user by ID
    @Override
    public User findById(UUID id) {
        // Check if the user with the given ID exists, return null if not
        if (userRepo.findById(id).isPresent()) {
            return userRepo.findById(id).get();
        }
        return null;
    }

    // Get all users
    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    // Find and return the last 5 registered users, sorted by registration date in descending order
    @Override
    public List<User> findLast5User() {
        List<User> users = findAllUsers();
        return users.stream()
                .sorted(Comparator.comparing(User::getRegisterDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getUserImageById(UUID userId) {
        return userRepo.findById(userId).get().getProfileImg();
    }


}
