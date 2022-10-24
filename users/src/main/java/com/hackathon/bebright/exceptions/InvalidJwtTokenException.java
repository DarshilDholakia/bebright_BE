package com.hackathon.bebright.exceptions;

import javax.naming.AuthenticationException;

public class InvalidJwtTokenException extends AuthenticationException {

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
