package com.relay.api;

import com.relay.domain.WorkItem;

// Describes the JSON the client sends when changing status.
// For example: {"status":"IN_PROGRESS"}.
// The record doesn’t create an endpoint by itself—it’s just the shape of the 
// endpoint’s input.
public record ChangeWorkItemStatusRequest(WorkItem.Status status) {
    

    
}
