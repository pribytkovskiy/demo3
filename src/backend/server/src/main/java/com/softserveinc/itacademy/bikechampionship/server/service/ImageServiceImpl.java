package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.avatar.AvatarResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {
    private UserService userService;

    public ImageServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean saveUserAvatar(Long id, MultipartFile file) {
        User user = userService.getUserById(id);
        try {
            Byte[] byteObjects = new Byte[file.getBytes().length];
            int i = 0;

            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }

            user.setAvatar(byteObjects);
            userService.saveUser(user);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public AvatarResponse getUserAvatar(Long id) {
        return new AvatarResponse(userService.getUserById(id).getAvatar());
    }
}
