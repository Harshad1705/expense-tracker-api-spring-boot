package com.harrySpringBoot.expensetrackerapi.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
