package com.example.safehands.ApiResponse;

import lombok.Data;


public class ApiException extends RuntimeException{

    public ApiException(String message){
        super(message);
    }
}
