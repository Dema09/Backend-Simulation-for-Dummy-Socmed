package org.java.personal.project.service;

import org.java.personal.project.dto.response.StatusResponse;
import org.springframework.stereotype.Service;

@Service
public interface MentionService {

    StatusResponse findUserByUsername(String username);

}
