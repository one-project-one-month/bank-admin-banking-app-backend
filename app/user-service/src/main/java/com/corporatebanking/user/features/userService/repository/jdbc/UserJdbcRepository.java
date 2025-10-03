package com.corporatebanking.user.features.userService.repository.jdbc;

import java.util.List;
import com.corporatebanking.user.features.userService.models.User;

public interface UserJdbcRepository {
    User save(User user);
    List<User> getAllUsers();
}
