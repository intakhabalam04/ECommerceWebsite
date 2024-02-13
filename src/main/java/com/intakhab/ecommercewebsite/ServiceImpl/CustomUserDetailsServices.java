package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServices implements UserDetailsService {

    private final UserRepo userRepository;

    public CustomUserDetailsServices(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailUserPhone) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmailIdOrPhoneNumber(emailUserPhone, emailUserPhone, emailUserPhone);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username, email, or phone number: " + emailUserPhone);
        }

        Collection<? extends GrantedAuthority> authorities = authorities(user);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public Collection<? extends GrantedAuthority> authorities(User user) {
        String rolesString = user.getRole().toString();
        String[] rolesArray = rolesString.split(",");

        return Arrays.stream(rolesArray).map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim())).collect(Collectors.toList());
    }

}
