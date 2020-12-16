package org.java.personal.project.dto.response;

import org.springframework.http.HttpStatus;

public abstract class BaseResponse {
    private HttpStatus response;
    private String status;

    public BaseResponse() {
    }

    public BaseResponse(HttpStatus response, String status) {
        this.response = response;
        this.status = status;
    }

    public HttpStatus getResponse() {
        return response;
    }

    public void setResponse(HttpStatus response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
