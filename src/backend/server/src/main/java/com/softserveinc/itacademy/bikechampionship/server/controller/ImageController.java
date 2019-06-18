package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.server.payload.user.avatar.AvatarResponse;
import com.softserveinc.itacademy.bikechampionship.server.service.ImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping("/users/{id}/image")
    public void getUserAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        AvatarResponse avatarResponse = imageService.getUserAvatar(id);
        if (avatarResponse.getFile() != null) {
            byte[] image = new byte[avatarResponse.getFile().length];

            int i = 0;
            for (Byte wrappedByte : avatarResponse.getFile()) {
                image[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(image);
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    @PostMapping("/users/{id}/image")
    public ResponseEntity uploadUserAvatar(@PathVariable Long id,
                                           @RequestParam("file") MultipartFile file) {
        if (imageService.saveUserAvatar(id, file)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
