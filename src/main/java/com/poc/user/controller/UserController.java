package com.poc.user.controller;


import com.poc.user.model.AuthenticateUserRequest;
import com.poc.user.model.AuthenticateUserResponse;
import com.poc.user.model.CreateUserResponse;
import com.poc.user.model.User;
import com.poc.user.repository.UserRepository;
import com.poc.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping(value = "/user" , produces = "application/json")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;
    private UserService uService;

    public UserController(UserRepository userRepository,
                          UserService uService) {
        this.userRepository = userRepository;
        this.uService = uService;
    }


    @ApiOperation(value = "User List")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        LOG.info("Getting all users.");
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userName) {
        LOG.info("Getting user with ID: {}.", userName);
        return userRepository.findOne(userName);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<CreateUserResponse> addNewUsers(@RequestBody User user) {

        Object obj = null;
        CreateUserResponse cur = new CreateUserResponse();
        System.out.println("obj - " + obj );
        System.out.println("emp.getUserName() - " + user.getUserName());
        obj = userRepository.findOne(user.getUserName());
        if(obj == null) {
            LOG.info("Saving user.");
            userRepository.save(user);
            cur.setMessage("User Created Successfully");
            return ResponseEntity.ok(cur);
        }
        cur.setMessage("User Id Already Exist");
        return ResponseEntity.ok(cur);
    }

    @RequestMapping(value = "/authenticate/user", method = RequestMethod.PUT)
    public ResponseEntity<AuthenticateUserResponse> authUserUsers(@RequestBody AuthenticateUserRequest request) {
        AuthenticateUserResponse authUserResponse = new AuthenticateUserResponse();
        User user = null;
        System.out.println("user details " + request.getApplicationId() + " " + request.getUserId() + " " + request.getPassword());
        user  = userRepository.findOne(request.getUserId());

        if (user!=null) {
            authUserResponse = uService.authenticateUser(user, request);
            System.out.println(authUserResponse.getMessage());
        }else{
        	System.out.println("Unable To Find User");
            authUserResponse.setMessage("Unable To Find User");
        }
        return ResponseEntity.ok(authUserResponse);
    }
}

