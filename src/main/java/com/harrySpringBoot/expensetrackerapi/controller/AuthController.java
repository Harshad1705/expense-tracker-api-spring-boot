package com.harrySpringBoot.expensetrackerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.harrySpringBoot.expensetrackerapi.entity.AuthModel;
import com.harrySpringBoot.expensetrackerapi.entity.JwtResponse;
import com.harrySpringBoot.expensetrackerapi.entity.User;
import com.harrySpringBoot.expensetrackerapi.entity.UserModel;
import com.harrySpringBoot.expensetrackerapi.security.CustomUserDetailService;
import com.harrySpringBoot.expensetrackerapi.service.UserService;
import com.harrySpringBoot.expensetrackerapi.util.JwtTokenUtil;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailService userDetailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception {


        authenticate(authModel.getEmail(), authModel.getPassword());

        // we need to generate the jwt token
        final  UserDetails userDetails= userDetailService.loadUserByUsername(authModel.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);


        return new ResponseEntity<JwtResponse>(new JwtResponse(token),HttpStatus.OK);
    }

     private void authenticate(String email, String password) throws Exception {
         try {
            authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            
        } catch (DisabledException e) {
            throw new Exception("User disabled");
        } catch (BadCredentialsException e) {
            throw new Exception("Bad Credential");
        } 
    }

    @PostMapping("/register")
    public ResponseEntity<User> save(@Valid @RequestBody UserModel user) {
        return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
    }
}


