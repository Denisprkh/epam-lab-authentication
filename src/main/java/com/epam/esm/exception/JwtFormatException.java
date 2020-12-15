package com.epam.esm.exception;


public class JwtFormatException extends RuntimeException{

    public JwtFormatException(String message){
        super(message);
    }

}
