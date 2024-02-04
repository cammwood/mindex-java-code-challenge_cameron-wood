package com.mindex.challenge.exceptions;

/**
 * Custom exception for when an empty result is found for a search
 */
public class EmptyObjectException extends RuntimeException {

    public EmptyObjectException(String message) {
        super(message);
    }
}