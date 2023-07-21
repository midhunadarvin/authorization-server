package com.chistadata.authorizationframework.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private boolean success;
    private String message;

    // Constructors

    public ErrorResponse() {
        this.success = false;
    }

    public ErrorResponse(String message) {
        this.success = false;
        this.message = message;
    }
}

