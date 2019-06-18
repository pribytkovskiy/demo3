package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.model.User;

public interface UserService {

    Boolean isEmailAvailable(String email);

    User getUserById(Long id);

    User editUserProfile(User user);
    
    User changeUserPassword(User user);

    void saveUser(User user);
}
