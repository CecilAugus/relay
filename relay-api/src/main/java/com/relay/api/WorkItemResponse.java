package com.relay.api;

import java.time.Instant;
import java.util.UUID;

import com.relay.domain.WorkItem;

// Describes the JSON the API sends back
// A response record gives Spring/Jackson an explicit, straightf. shape to serialize
public record WorkItemResponse(
    UUID id,
    String title,
    Instant creationTime,
    WorkItem.Status status    
) {}
