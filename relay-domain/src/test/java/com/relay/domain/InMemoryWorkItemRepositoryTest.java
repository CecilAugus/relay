package com.relay.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryWorkItemRepositoryTest {

  private static final Instant CREATED_AT =
      Instant.parse("2026-01-01T10:00:00Z");

  @Test
  void savesAndFindsAWorkItemById() {
    InMemoryWorkItemRepository repository =
        new InMemoryWorkItemRepository();
    WorkItem workItem = new WorkItem("Document the API", CREATED_AT);

    repository.save(workItem);

    assertEquals(
        workItem,
        repository.findById(workItem.id()).orElseThrow());
  }

  @Test
  void returnsEmptyForAnUnknownId() {
    InMemoryWorkItemRepository repository =
        new InMemoryWorkItemRepository();

    assertFalse(repository.findById(UUID.randomUUID()).isPresent());
  }

  @Test
  void returnsAllSavedWorkItems() {
    InMemoryWorkItemRepository repository =
        new InMemoryWorkItemRepository();
    WorkItem first = new WorkItem("Document the API", CREATED_AT);
    WorkItem second = new WorkItem("Add API tests", CREATED_AT);

    repository.save(first);
    repository.save(second);

    assertEquals(2, repository.findAll().size());
    assertEquals(
        2,
        repository.findAll().stream()
            .filter(item -> item == first || item == second)
            .count());
  }

  @Test
  void rejectsNullWorkItems() {
    InMemoryWorkItemRepository repository =
        new InMemoryWorkItemRepository();

    assertThrows(
        IllegalArgumentException.class,
        () -> repository.save(null));
  }

  @Test
  void returnsEmptyForNullId() {
    InMemoryWorkItemRepository repository =
        new InMemoryWorkItemRepository();

    assertFalse(repository.findById(null).isPresent());
  }

  @Test
  void returnedItemsCannotModifyRepository() {
    InMemoryWorkItemRepository repository =
        new InMemoryWorkItemRepository();
    WorkItem workItem = new WorkItem("Document the API", CREATED_AT);
    repository.save(workItem);

    Collection<WorkItem> result = repository.findAll();

    assertThrows(
        UnsupportedOperationException.class,
        result::clear);
    assertEquals(1, repository.findAll().size());
  }
}
