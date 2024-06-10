package com.intakhab.ecommercewebsite.Repository;

import com.intakhab.ecommercewebsite.Enum.UserRole;
import com.intakhab.ecommercewebsite.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User,UUID> {
    User findByUsername(String username );
    User findByEmailId(String emailId   );
    User findByPhoneNumber(String phoneNumber);
    User findByUsernameOrEmailIdOrPhoneNumber(String userName, String emailId, String phoneNumber);
    User findByToken(String token);

    List<User> findByRole(UserRole role);
}
