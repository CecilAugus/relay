package com.relay.api;

import jakarta.validation.constraints.NotBlank;

// @NotBlank rejects null, empty, or whitespace-only titles.
public record CreateWorkItemRequest(
    @NotBlank(message = "title must not be blank") String title
) {}