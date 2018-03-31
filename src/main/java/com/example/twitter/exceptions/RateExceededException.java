package com.example.twitter.exceptions;

public class RateExceededException extends Exception{

    public RateExceededException(String message) {
        super(message);
    }
}
