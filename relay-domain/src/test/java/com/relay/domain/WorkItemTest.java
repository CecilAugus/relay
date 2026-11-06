
package com.relay.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class WorkItemTest {

  @Test
  void createsAnOpenWorkItem() {
    Instant createdAt = Instant.parse("2026-01-01T10:00:00Z");

    WorkItem workItem = new WorkItem("Document the API", createdAt);

    assertNotNull(workItem.id());
    assertEquals("Document the API", workItem.title());
    assertEquals(createdAt, workItem.creationTime());
    assertEquals(WorkItem.Status.OPEN, workItem.status());
  }

  @Test
  void movesThroughTheValidWorkflow() {
    WorkItem workItem = new WorkItem(
        "Document the API",
        Instant.parse("2026-01-01T10:00:00Z"));

    workItem.moveTo(WorkItem.Status.IN_PROGRESS);
    assertEquals(WorkItem.Status.IN_PROGRESS, workItem.status());

    workItem.moveTo(WorkItem.Status.DONE);
    assertEquals(WorkItem.Status.DONE, workItem.status());
  }

  @Test
  void rejectsSkippingInProgress() {
    WorkItem workItem = new WorkItem(
        "Document the API",
        Instant.parse("2026-01-01T10:00:00Z"));

    assertThrows(
        IllegalStateException.class,
        () -> workItem.moveTo(WorkItem.Status.DONE));
  }

  @Test
  void rejectsMovingDoneBackToOpen() {
    WorkItem workItem = new WorkItem(
        "Document the API",
        Instant.parse("2026-01-01T10:00:00Z"));
    workItem.moveTo(WorkItem.Status.IN_PROGRESS);
    workItem.moveTo(WorkItem.Status.DONE);

    assertThrows(
        IllegalStateException.class,
        () -> workItem.moveTo(WorkItem.Status.OPEN));
  }

  @Test
  void rejectsMovingInProgressBackToOpen() {
    WorkItem workItem = new WorkItem(
        "Document the API",
        Instant.parse("2026-01-01T10:00:00Z"));
    workItem.moveTo(WorkItem.Status.IN_PROGRESS);

    assertThrows(
        IllegalStateException.class,
        () -> workItem.moveTo(WorkItem.Status.OPEN));
  }

  @Test
  void rejectsNullStatus() {
    WorkItem workItem = new WorkItem(
        "Document the API",
        Instant.parse("2026-01-01T10:00:00Z"));

    assertThrows(
        IllegalArgumentException.class,
        () -> workItem.moveTo(null));
  }

  @Test
  void rejectsBlankTitles() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new WorkItem("   ", Instant.now()));
  }

  @Test
  void rejectsNullCreationTime() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new WorkItem("Document the API", null));
  }
}
