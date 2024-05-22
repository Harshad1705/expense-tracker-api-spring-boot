package com.harrySpringBoot.expensetrackerapi.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.harrySpringBoot.expensetrackerapi.repository.UserRepository;
import com.harrySpringBoot.expensetrackerapi.entity.User;

@Service
public class CustomUserDetailService implements UserDetailsService {


    @Autowired
    private UserRepository UserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       
        User existingUser = UserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for the email : " + email));

        System.out.println(existingUser);

        return new org.springframework.security.core.userdetails.User(existingUser.getEmail(), existingUser.getPassword(),
                new ArrayList<>());
    }

}
