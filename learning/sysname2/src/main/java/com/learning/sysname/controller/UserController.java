package com.learning.sysname.controller;


import com.learning.sysname.entity.User;
import com.learning.sysname.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//localhost:8080/user/create
@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    UserService userService;

    @PostMapping("create")
    public User create(@RequestBody User user) {
        return userService.create(user);
    }
    @GetMapping("find-by-id")
    public User read(@RequestParam Long id){
        return userService.read(id);
    }

    @PostMapping("update")
    public User update(@RequestBody User user){
        return userService.update(user);
    }


    @DeleteMapping("delete")
    public boolean delete(@RequestParam Long id){
        return userService.delete(id);
    }

}
