package com.epam.esm.exception;

public class ResourceNotFoundException extends RuntimeException {

    private int resourceId;

    public ResourceNotFoundException(String message, int resourceId){
        super(message);
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public int getResourceId() {
        return resourceId;
    }
}