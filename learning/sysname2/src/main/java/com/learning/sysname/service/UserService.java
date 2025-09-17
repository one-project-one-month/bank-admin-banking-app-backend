package com.learning.sysname.service;


import com.learning.sysname.entity.User;
import com.learning.sysname.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User create(User user) {

        return userRepository.save(user);
    }


    public User read(Long id){
        Optional<User> userOp = userRepository.findById(id);
        if(userOp.isPresent()){
            return userOp.get();
        }else return null;
    }

    public User update(User user){
        return userRepository.save(user);
    }

    public boolean delete(Long id){
        Optional<User> userOp = userRepository.findById(id);
        if(userOp.isPresent()){
            userRepository.delete(userOp.get());
            return true;
        }else return false;
    }
}
