package com.relay.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.relay.application.WorkItemNotFoundException;
import com.relay.domain.InvalidWorkItemTransitionException;

// Spring discovers this advice and lets it handle exceptions raised while
// processing requests in controller classes.
@RestControllerAdvice 
public class ApiExceptionHandler {

    // A valid item ID with no matching work item becomes HTTP 404.
    // Spring calls this method when that specific exception reaches the web layer.
    @ExceptionHandler(WorkItemNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(
        WorkItemNotFoundException exception 
    ) {
        return ResponseEntity.notFound().build();
    }

    // An item exists, but its current state conflicts with the requested
    // transition. HTTP 409 describes that conflict.
    @ExceptionHandler(InvalidWorkItemTransitionException.class)
    public ResponseEntity<Void> handleInvalidTransition(
        InvalidWorkItemTransitionException exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    
}
