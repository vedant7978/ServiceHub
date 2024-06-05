package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.UserAlreadyExistException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.service.UserService;
import com.dalhousie.servicehub.util.AuthenticationRequest;
import com.dalhousie.servicehub.util.AuthenticationResponse;
import com.dalhousie.servicehub.util.RegisterRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<Object> userRegisterHandler(@Valid @RequestBody RegisterRequest regReq) {
        try {
            if (userRepository.findByEmail(regReq.getEmail()).isPresent()) {
                throw new UserAlreadyExistException("User with this email already exists.");
            }
            UserModel userModel = modelMapper.map(regReq, UserModel.class);
            AuthenticationResponse authRes = userService.registerUser(userModel);
            logger.info("User registered successfully with email: {}", regReq.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(authRes);
        } catch (UserAlreadyExistException e) {
            logger.error("User registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> userLoginHandler(@Valid @RequestBody AuthenticationRequest authReq) {
        try {
            if (userRepository.findByEmail(authReq.getEmail()).isEmpty()) {
                throw new UsernameNotFoundException("User with this email doesn't exists.");
            }
            AuthenticationResponse authRes = userService.authenticateUser(authReq);
            logger.info("User login successful for email: {}", authReq.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(authRes);
        } catch (UsernameNotFoundException e) {
            logger.error("User login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during user login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
