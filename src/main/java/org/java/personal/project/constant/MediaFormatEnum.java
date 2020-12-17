package org.java.personal.project.constant;

public enum MediaFormatEnum {
    JPEG(".jpeg"),
    JPG(".jpg"),
    PNG(".png"),
    MP4(".mp4"),
    AVI(".avi"),
    MOV(".mov");

    private String message;

    MediaFormatEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
