package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.server.payload.user.avatar.AvatarResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    boolean saveUserAvatar(Long id, MultipartFile file);

    AvatarResponse getUserAvatar(Long id);
}
