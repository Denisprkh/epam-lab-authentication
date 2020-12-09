package com.epam.esm.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    private int resourceId;

    public ResourceAlreadyExistsException(String message, int resourceId) {
        super(message);
        this.resourceId = resourceId;
    }

    public ResourceAlreadyExistsException(String message){
        super(message);
    }

    public int getResourceId() {
        return resourceId;
    }
}