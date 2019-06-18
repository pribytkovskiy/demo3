package com.softserveinc.itacademy.bikechampionship.server.config;

import com.softserveinc.itacademy.bikechampionship.server.converter.CategoryRequestToCategoryConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.CategoryToCategoryResponseConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.EventRequestToEventConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.EventToEventResponseConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.ParticipantToResponseConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.RequestToParticipantConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.SignUpRequestToUserConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.user.UserPasswordToUserConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.user.UserPrincipalToCurrentUserResponseConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.user.UserProfileToUserConverter;
import com.softserveinc.itacademy.bikechampionship.server.converter.user.UserToUserProfileConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private ConversionService conversionService;

    @Lazy
    public WebConfig(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(userPrincipalToCurrentUserResponseConverter());
        registry.addConverter(userToUserProfileConverter());
        registry.addConverter(categoryToCategoryResponseConverter());
        registry.addConverter(categoryRequestToCategoryConverter());
        registry.addConverter(eventToEventResponseConverter());
        registry.addConverter(eventRequestToEventConverter());
        registry.addConverter(participantToResponseConverter());
        registry.addConverter(userProfileToUserConverter());
        registry.addConverter(requestToParticipantConverter());
        registry.addConverter(userPasswordToUserConverter());
    }

    @Bean
    public UserPrincipalToCurrentUserResponseConverter userPrincipalToCurrentUserResponseConverter() {
        return new UserPrincipalToCurrentUserResponseConverter();
    }

    @Bean
    public UserToUserProfileConverter userToUserProfileConverter() {
        return new UserToUserProfileConverter();
    }

    @Bean
    public EventRequestToEventConverter eventRequestToEventConverter() {
        return new EventRequestToEventConverter(conversionService);
    }

    @Bean
    public EventToEventResponseConverter eventToEventResponseConverter() {
        return new EventToEventResponseConverter(conversionService);
    }

    @Bean
    public CategoryToCategoryResponseConverter categoryToCategoryResponseConverter() {
        return new CategoryToCategoryResponseConverter();
    }

    @Bean
    public CategoryRequestToCategoryConverter categoryRequestToCategoryConverter() {
        return new CategoryRequestToCategoryConverter();
    }

    @Bean
    public ParticipantToResponseConverter participantToResponseConverter() {
        return new ParticipantToResponseConverter(conversionService);
    }

    @Bean
    public RequestToParticipantConverter requestToParticipantConverter() {
        return new RequestToParticipantConverter();
    }

    @Bean
    public SignUpRequestToUserConverter signUpRequestToUserConverter() {
        return new SignUpRequestToUserConverter();
    }

    @Bean
    public UserProfileToUserConverter userProfileToUserConverter() {
        return new UserProfileToUserConverter();
    }

    @Bean
    public UserPasswordToUserConverter userPasswordToUserConverter() {
        return new UserPasswordToUserConverter();
    }

}
