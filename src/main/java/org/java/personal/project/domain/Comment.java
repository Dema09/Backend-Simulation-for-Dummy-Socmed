package org.java.personal.project.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Comment {
    private String comment;

    @DBRef
    private DummyUser author;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DummyUser getAuthor() {
        return author;
    }

    public void setAuthor(DummyUser author) {
        this.author = author;
    }
}
