package org.java.personal.project.controller;

import org.java.personal.project.dto.MentionUserHeadResponse;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.MentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class MentionController {

    private final MentionService mentionService;

    @Autowired
    public MentionController(MentionService mentionService) {
        this.mentionService = mentionService;
    }

    @GetMapping(value = "/searchUser/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity findUserByUsernameController(@PathVariable String username){
        StatusResponse findUserResponse = mentionService.findUserByUsername(username);
        return new ResponseEntity(findUserResponse, HttpStatus.OK);
    }
}
