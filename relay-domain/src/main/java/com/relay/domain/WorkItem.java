package com.relay.domain;

import java.time.Instant;
import java.util.UUID;

public final class WorkItem {

  public enum Status {
    OPEN,
    IN_PROGRESS,
    DONE,
  }

  private final UUID id;
  private final String title;
  private final Instant creationTime;
  private Status status;

  public WorkItem(
      String title,
      Instant creationTime) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("title is required");
    }

    if (creationTime == null) {
      throw new IllegalArgumentException(
          "creationTime is required");
    }

    this.id = UUID.randomUUID();
    this.title = title;
    this.creationTime = creationTime;
    this.status = Status.OPEN;
  }

  private WorkItem(
    UUID id, 
    String title,
    Instant creationTime, 
    Status status 
  ) {
        
    // A restored item must have its original identifier and status.
    if (id == null) {
      throw new IllegalArgumentException("id is required");
    }
    if (status == null) {
      throw new IllegalArgumentException("status is required");
    }

    // Reuse the same validation rules as creating a new item.
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("title is required");
    }
    if (creationTime == null) {
      throw new IllegalArgumentException("creationTime is required");
    }

    this.id = id;
    this.title = title;
    this.creationTime = creationTime;
    this.status = status;
  }

  public static WorkItem restore(
    UUID id, 
    String title,
    Instant creationTime, 
    Status status 
  ) {
      return new WorkItem(id, title, creationTime, status);
  }

  public void moveTo(Status nextStatus) {
    if (nextStatus == null) {
      throw new IllegalArgumentException("nextStatus can't be null");
    }

    boolean validTransition = status == Status.OPEN
        && nextStatus == Status.IN_PROGRESS
        || status == Status.IN_PROGRESS
        && nextStatus == Status.DONE;

    if (!validTransition) {
      throw new InvalidWorkItemTransitionException(
          "Cannot move from " + status + " to " + nextStatus
        );
    }

    status = nextStatus;
  }

  public UUID id() {
    return id;
  }

  public String title() {
    return title;
  }

  public Instant creationTime() {
    return creationTime;
  }

  public Status status() {
    return status;
  }

}
