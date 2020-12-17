package org.java.personal.project.constant;

public enum AppEnum {
    YOUR_USERNAME_WITH_ID("Your username with id: "),
    IS_NOT_EXISTS(" is not exists!"),
    POST_HAS_BEEN_CREATED("Your post has been created!"),
    PICTURE_CANNOT_LOAD_PROPERLY("Your picture post can't load properly!"),
    POST_NOT_FOUND("The post that you're looking for is either not found or has been deleted by user!"),
    USER_NOT_FOUND("No matched user with the id: "),
    SUCCESSFULLY_ADD_COMMENT("Successfully add your comment!"),
    POST_WITH_ID("This post with id: "),
    LIKED_BY(" liked by: "),
    INVALID_POST_FORMAT("Your posts format is invalid!"),
    CAN_NOT_ENCODE_THE_VIDEO("Can't encode the video");

    private String message;

    AppEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
