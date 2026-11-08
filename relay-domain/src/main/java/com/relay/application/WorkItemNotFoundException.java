package com.relay.application;

// A valid ID was provided, but no item with that ID exists
public class WorkItemNotFoundException extends RuntimeException {

    public WorkItemNotFoundException(String message) {
        super(message);
    }

}
