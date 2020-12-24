package org.java.personal.project.service.impl;

import org.java.personal.project.dao.UserRepository;
import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.dto.MentionUserHeadResponse;
import org.java.personal.project.dto.response.MentionUserResponse;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.MentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MentionServiceImpl implements MentionService {

    private final UserRepository userRepository;

    @Autowired
    public MentionServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public StatusResponse findUserByUsername(String username) {
        StatusResponse statusResponse = new StatusResponse();
        MentionUserHeadResponse mention = new MentionUserHeadResponse();
        List<MentionUserResponse> mentionUserResponses = new ArrayList<>();

        List<DummyUser> currentMentionedUsers = userRepository.findDummyUsersByUsernameLikeOrderByUsernameAsc(username);
        if(currentMentionedUsers.size() == 0)
            return statusResponse.statusOk(new MentionUserHeadResponse());

        for(DummyUser dummyUser : currentMentionedUsers){
            MentionUserResponse mentionUserResponse = new MentionUserResponse();
            mentionUserResponse.setUserId(dummyUser.getId());
            mentionUserResponse.setUsername(dummyUser.getUsername());

            mentionUserResponses.add(mentionUserResponse);
        }
        mention.setMentionedUsers(mentionUserResponses);

        return statusResponse.statusOk(mention);
    }
}
