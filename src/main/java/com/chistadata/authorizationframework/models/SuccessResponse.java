package com.chistadata.authorizationframework.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // Constructors

    public SuccessResponse() {
        this.success = true;
    }

    public SuccessResponse(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
    }
}

