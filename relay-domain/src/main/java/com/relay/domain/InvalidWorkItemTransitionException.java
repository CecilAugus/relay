package com.relay.domain;

// Item exists, but current status doesn't allow requested transition
public class InvalidWorkItemTransitionException extends RuntimeException {
    
    public InvalidWorkItemTransitionException(String message) {
        super(message);
    }

}
