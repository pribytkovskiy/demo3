package com.softserveinc.itacademy.bikechampionship.server.payload.user.avatar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvatarResponse {

    Byte[] file;
}
