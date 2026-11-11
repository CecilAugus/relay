package com.relay.api.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.relay.domain.WorkItem;

class WorkItemEntityTest {

    @Test
    void convertsToEntityAndBackWithoutLosingWorkItemState() {
        // Use a non-default status so the test verifies that restoration
        // preserves more than the fields created by a new WorkItem.
        WorkItem original = new WorkItem(
                "Persist a work item",
                Instant.parse("2026-11-11T18:00:00Z"));
        original.moveTo(WorkItem.Status.IN_PROGRESS);

        // Convert to the persistence representation and then back to the domain.
        WorkItemEntity entity = new WorkItemEntity(original);
        WorkItem restored = entity.convertIntoDomain();

        // All persisted state should survive the round trip, including identity
        // and status.
        assertEquals(original.id(), restored.id());
        assertEquals(original.title(), restored.title());
        assertEquals(original.creationTime(), restored.creationTime());
        assertEquals(original.status(), restored.status());
    }
}
