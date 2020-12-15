package org.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_data")
public class DummyUser {
    @Id
    private String id;

    private String username;


    public DummyUser(String id, String username) {
        this.id = id;
        this.username = username;
    }
}
