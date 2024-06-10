package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Enum.UserRole;
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
        return userLists.stream().filter(user -> user.getRole().equals(UserRole.USER)).collect(Collectors.toList());
    }

    @Override
    public User findById(UUID id) {
        // Check if the user with the given ID exists, return null if not
        if (userRepo.findById(id).isPresent()) {
            return userRepo.findById(id).get();
        }
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public List<User> findLast5User() {
        List<User> users = findAllUsers();
        return users.stream().sorted(Comparator.comparing(User::getRegisterDate).reversed()).limit(5).collect(Collectors.toList());
    }

    @Override
    public byte[] getUserImageById(UUID userId) {
        return userRepo.findById(userId).get().getProfileImg();
    }


}
