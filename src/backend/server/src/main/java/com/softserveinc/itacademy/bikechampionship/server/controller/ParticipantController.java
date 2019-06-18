package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.commons.exception.ForbiddenException;
import com.softserveinc.itacademy.bikechampionship.commons.exception.UnauthorizedException;
import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;
import com.softserveinc.itacademy.bikechampionship.server.payload.participant.ParticipantRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.participant.ParticipantResponse;
import com.softserveinc.itacademy.bikechampionship.server.security.CurrentUser;
import com.softserveinc.itacademy.bikechampionship.server.security.UserPrincipal;
import com.softserveinc.itacademy.bikechampionship.server.service.ParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/participants")
@Api("/api/participants")
public class ParticipantController {
    private final ParticipantService participantService;
    private final ConversionService conversionService;

    @Autowired
    public ParticipantController(ParticipantService participantService,
                                 ConversionService conversionService) {
        this.participantService = participantService;
        this.conversionService = conversionService;
    }

    @ApiOperation(value = "create new participant")
    @PostMapping
    public ResponseEntity<ParticipantResponse> createParticipant(@Valid @RequestBody ParticipantRequest participantRequest,
                                                                 @CurrentUser UserPrincipal userPrincipal) {
        if (participantRequest.getUserId() != null && participantRequest.getUserId() > 0L) {
            if (userPrincipal == null) {
                log.info("Attempt to participate as registered user from unauthorized context");
                throw new UnauthorizedException("You can not participate as registered user. Login and retry.");
            }
            if (!userPrincipal.getId().equals(participantRequest.getUserId())) {
                log.warn(userPrincipal.getEmail() + " attempts to participate as another user");
                throw new ForbiddenException("You can not participate as another user");
            }
        }
        Participant participant = conversionService.convert(participantRequest, Participant.class);
        Participant createdParticipant = participantService.createParticipant(participant);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(createdParticipant.getId()).toUri();
        return ResponseEntity.created(location).body(conversionService.convert(createdParticipant, ParticipantResponse.class));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ParticipantResponse>> getParticipantsByUserId(@RequestParam("userId") Long userId) {
        List<ParticipantResponse> participantResponses = participantService.getParticipantsByUserId(userId).stream()
                .map(participant -> conversionService.convert(participant, ParticipantResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(participantResponses);
    }
}
