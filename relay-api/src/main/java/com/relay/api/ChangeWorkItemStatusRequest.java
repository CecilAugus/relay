package com.relay.api;

import com.relay.domain.WorkItem;

import jakarta.validation.constraints.NotNull;

// Describes the JSON the client sends when changing status.
// For example: {"status":"IN_PROGRESS"}.
// The record doesn’t create an endpoint by itself—it’s just the shape of the 
// endpoint’s input.
public record ChangeWorkItemStatusRequest(@NotNull WorkItem.Status status) {}
