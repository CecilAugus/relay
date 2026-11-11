package com.relay.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.relay.domain.InMemoryWorkItemRepository;
import com.relay.domain.WorkItem;
import com.relay.domain.WorkItemRepository;

class WorkItemServiceTest {

    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");
    private static final String TITLE = "Document the API";

    private WorkItemRepository repository;
    private WorkItemService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkItemRepository();
        service = new WorkItemService(repository);
    }

    @Test
    void createSavesAndReturnsWorkItem() {
        WorkItem created = service.create(TITLE, CREATED_AT);

        assertNotNull(created);
        assertEquals(TITLE, created.title());
        assertEquals(CREATED_AT, created.creationTime());
        assertEquals(WorkItem.Status.OPEN, created.status());
    }

    @Test
    void createThrowsIfTitleIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.create(null, CREATED_AT)
        );
    }

    @Test
    void createThrowsIfCreationTimeIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.create(TITLE, null)
        );
    }

    @Test
    void findByIdReturnsItemIfFound() {
        WorkItem created = service.create(TITLE, CREATED_AT);

        var found = service.findById(created.id());

        assertTrue(found.isPresent());
        assertEquals(created.id(), found.get().id());
        assertEquals(TITLE, found.get().title());
    }

    @Test
    void findByIdReturnsEmptyIfNotFound() {
        var found = service.findById(UUID.randomUUID());

        assertTrue(found.isEmpty());
    }

    @Test
    void findAllWorkItemsReturnsAllSavedItems() {
        // Arrange: save two distinct items through the service.
        WorkItem first = service.create(TITLE, CREATED_AT);
        WorkItem second = service.create("Add tests", CREATED_AT);

        // Act: ask the service for every work item.
        Collection<WorkItem> found = service.findAllWorkItems();

        // Assert: both saved items are present in the returned collection.
        assertEquals(2, found.size());
        assertTrue(found.contains(first));
        assertTrue(found.contains(second));
    }

    @Test
    void removeWorkItemDeletesExistingItem() {
        // Arrange: create an item so the repository has something to remove.
        WorkItem created = service.create(TITLE, CREATED_AT);

        // Act: ask the service to remove it.
        service.removeWorkItem(created.id());

        // Assert: the item is no longer returned by the repository-backed lookup.
        assertTrue(service.findById(created.id()).isEmpty());
    }

    @Test
    void removeWorkItemThrowsIfIdIsNull() {
        // A missing ID argument is invalid input, not a not-found resource.
        assertThrows(
                IllegalArgumentException.class,
                () -> service.removeWorkItem(null));
    }

    @Test
    void removeWorkItemThrowsIfItemDoesNotExist() {
        // A valid but unknown ID represents a requested resource that is absent.
        UUID nonexistentId = UUID.randomUUID();

        assertThrows(
                WorkItemNotFoundException.class,
                () -> service.removeWorkItem(nonexistentId));
    }

    @Test
    void moveToChangesStatus() {
        WorkItem created = service.create(TITLE, CREATED_AT);

        service.moveTo(created.id(), WorkItem.Status.IN_PROGRESS);

        var found = service.findById(created.id());
        assertTrue(found.isPresent());
        assertEquals(WorkItem.Status.IN_PROGRESS, found.get().status());
    }

    @Test
    void moveToThrowsIfIdIsNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.moveTo(null, WorkItem.Status.IN_PROGRESS)
        );
    }

    @Test
    void moveToThrowsIfNextStatusIsNull() {
        WorkItem created = service.create(TITLE, CREATED_AT);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.moveTo(created.id(), null)
        );
    }

    @Test
    void moveToThrowsIfItemNotFound() {
        UUID nonexistentId = UUID.randomUUID();

        assertThrows(
            WorkItemNotFoundException.class,
            () -> service.moveTo(nonexistentId, WorkItem.Status.IN_PROGRESS)
        );
    }
}
