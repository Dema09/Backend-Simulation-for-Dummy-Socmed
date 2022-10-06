package org.java.personal.project.dto.response;

import org.java.personal.project.enumeration.StatusEnum;
import org.springframework.http.HttpStatus;

public class StatusResponse extends BaseResponse{
    private String message;
    private Object data;

    public StatusResponse() {}

    public StatusResponse(HttpStatus response, String status, String message, Object data) {
        super(response, status);
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public StatusResponse statusInternalServerError(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                StatusEnum.INTERNAL_SERVER_ERROR.getMessage(),
                message,
                data
        );

        return statusResponse;
    }

    public StatusResponse statusNotFound(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.NOT_FOUND,
                StatusEnum.NOT_FOUND.getMessage(),
                message,
                data
        );
        return statusResponse;
    }

    public StatusResponse statusOk(Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.OK,
                StatusEnum.OK.getMessage(),
                "Ok",
                data
        );
        return statusResponse;
    }

    public StatusResponse statusCreated(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.CREATED,
                StatusEnum.CREATED.getMessage(),
                message,
                data
        );
        return statusResponse;
    }

    public StatusResponse statusUnauthorized(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.UNAUTHORIZED,
                StatusEnum.UNAUTHORIZED.getMessage(),
                message,
                data
        );
        return statusResponse;
    }

    public StatusResponse statusBadRequest(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.BAD_REQUEST,
                StatusEnum.BAD_REQUEST.getMessage(),
                message,
                null
        );
        return statusResponse;
    }

    public StatusResponse statusConflict(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.CONFLICT,
                StatusEnum.CONFLICT.getMessage(),
                message,
                data
        );
        return statusResponse;
    }
}
