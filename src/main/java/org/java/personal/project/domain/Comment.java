package org.java.personal.project.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Document(collection = "comment")
public class Comment {
    @Id
    private String commentId;

    private String comment;

    @DBRef
    private DummyUser author;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date lastModified;

    @DBRef
    private Post post;

    private boolean isUpdated;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
