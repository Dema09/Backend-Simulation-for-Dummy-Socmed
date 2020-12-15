package org.java.personal.project.constant;

public enum AppEnum {
    YOUR_USERNAME_WITH_ID("Your username with id: "),
    IS_NOT_EXISTS(" is not exists!"),
    POST_HAS_BEEN_CREATED("Your post has been created!");
    private String message;

    AppEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
