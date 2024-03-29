package org.java.personal.project.enumeration;

public enum AppEnum {
    THIS_USER_WITH_ID("Your username with id: "),
    IS_NOT_EXISTS(" is not exists!"),
    POST_HAS_BEEN_CREATED("Your post has been created!"),
    PICTURE_CANNOT_LOAD_PROPERLY("Your picture post can't load properly!"),
    POST_NOT_FOUND("The post that you're looking for is either not found or has been deleted by user!"),
    STORY_NOT_FOUND("Story that you're looking for is either not found or has been deleted by user!"),
    USER_NOT_FOUND("No matched user with the id: "),
    SUCCESSFULLY_DELETE_STORY("Successfully delete story!"),
    SUCCESSFULLY_ADD_COMMENT("Successfully add your comment!"),
    POST_WITH_ID("This post with id: "),
    LIKED_BY(" liked by: "),
    INVALID_POST_FORMAT("Your posts format is invalid!"),
    CAN_NOT_ENCODE_THE_VIDEO("Can't encode the video"),
    UPDATE_CAPTION_SUCCESSFULLY("Successfully updated your post's caption with post id: "),
    DELETE_POST_SUCCESSFULLY("Successfully delete your post with id: "),
    STORY_HAS_BEEN_CREATED("Your story has been created!"),
    SUCCESSFULLY_CREATE_COLLECTION("Successfully create collection!"),
    SUCCESSFULLY_UPDATE_COLLECTION_WITH_COLLECTION_ID("Successfully update your collection with collection id: "),
    USER_STORY_IS_NOT_FOUND("Cannot find the story collection with id: "),
    THIS_MENTIONED_USER_WITH_ID("This mentioned user with id: "),
    YOUR_POST_COLLECTION("Your Post Collection"),
    SUCCESSFULLY_INSERT_OR_UPDATE_POST_COLLECTION("Successfully insert or update your post collection!"),
    SUCCESSFULLY_UPDATE_POST_COLLECTION_NAME("Successfully update your post collection's name!"),
    POINT("Point"),
    SUCCESSFULLY_UPDATE_YOUR_POST_COLLECTION_WITH_ID("Successfully update your post collection with id: "),
    SUCCESSFULLY_UNLIKE_POST("Successfully unlike post!");


    private String message;

    AppEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
