package com.corporatebanking.auth.service;

import com.corporatebanking.auth.model.User;
import com.corporatebanking.auth.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.HashSet;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    public String authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (BcryptUtil.matches(password, user.password)) {
                return Jwt.issuer("https://corporatebanking.com/issuer")
                          .upn(user.username)
                          .groups(new HashSet<>())
                          .sign();
            }
        }
        return null;
    }
}
