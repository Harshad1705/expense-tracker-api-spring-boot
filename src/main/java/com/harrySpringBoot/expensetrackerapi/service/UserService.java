package com.harrySpringBoot.expensetrackerapi.service;

import com.harrySpringBoot.expensetrackerapi.entity.User;
import com.harrySpringBoot.expensetrackerapi.entity.UserModel;

public interface UserService {

    User createUser(UserModel user);

    User readUser();

    User updateUser(UserModel user);
    
    void deleteUser();

    User getLoggedInUser();

}
