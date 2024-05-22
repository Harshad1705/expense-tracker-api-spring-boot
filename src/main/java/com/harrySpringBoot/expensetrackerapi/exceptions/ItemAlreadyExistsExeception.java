
package com.harrySpringBoot.expensetrackerapi.exceptions;

public class ItemAlreadyExistsExeception extends RuntimeException {
    
    private static final long serialVersionID = 1L;

    public ItemAlreadyExistsExeception(String message) {
        super(message);
    }

}

