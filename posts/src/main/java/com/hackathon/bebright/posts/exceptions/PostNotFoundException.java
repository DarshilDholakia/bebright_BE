package com.hackathon.bebright.posts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message) {
        super(message);
    }
}