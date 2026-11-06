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

  public void moveTo(Status nextStatus) {
    if (nextStatus == null) {
      throw new IllegalArgumentException("nextStatus can't be null");
    }

    boolean validTransition = status == Status.OPEN
        && nextStatus == Status.IN_PROGRESS
        || status == Status.IN_PROGRESS
            && nextStatus == Status.DONE;

    if (!validTransition) {
      throw new IllegalStateException(
          "Cannot move from " + status + " to " + nextStatus);
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
