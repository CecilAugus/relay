package com.relay.application;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.relay.domain.WorkItem;
import com.relay.domain.WorkItemRepository;

public final class WorkItemService {

    private final WorkItemRepository repository;

    public WorkItemService(WorkItemRepository repository) {
        this.repository = repository;
    }


    public WorkItem create(
        String title,
        Instant creationTime
    ) {
        if (title == null) {
            throw new IllegalArgumentException(
                "Title can't be null"
            );
        }

        if (creationTime == null) {
            throw new IllegalArgumentException(
                "creationTime can't be null"
            );
        }

        var item = new WorkItem(title, creationTime);
        this.repository.save(item);
        return item; 
    }

    public Optional<WorkItem> findById(UUID id) {
        return repository.findById(id);
    }

    public void moveTo(UUID id, WorkItem.Status nextStatus) {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }

        if (nextStatus == null) {
            throw new IllegalArgumentException("nextStatus can't be null");
        }

        WorkItem item = repository.findById(id)
            .orElseThrow(() -> new WorkItemNotFoundException("Item not found"));

        item.moveTo(nextStatus);
    }
}