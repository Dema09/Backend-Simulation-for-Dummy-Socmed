package org.java.personal.project.enumeration;

public enum StatusEnum {
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    NOT_FOUND("Not Found"),
    OK("Ok"),
    UNAUTHORIZED("Unauthorized"),
    CREATED("Created"),
    BAD_REQUEST("Bad Request"),
    CONFLICT("Conflict");

    private String message;

    StatusEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
