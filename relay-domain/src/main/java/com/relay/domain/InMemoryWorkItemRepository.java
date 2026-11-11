package com.relay.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class InMemoryWorkItemRepository implements WorkItemRepository {

  private Map<UUID, WorkItem> workItems = new HashMap<>();

  public void save(WorkItem workItem) {
    if (workItem == null) {
      throw new IllegalArgumentException(
          "workItem cannot be null");
    }

    this.workItems.put(workItem.id(), workItem);
  }

  public Optional<WorkItem> findById(UUID id) {
    if (id == null) {
      return Optional.empty();
    }

    return Optional.ofNullable(this.workItems.get(id));
  }

  public Collection<WorkItem> findAll() {
    return List.copyOf(this.workItems.values());
  }

  public boolean remove(UUID id) {
    
    if (workItems.containsKey(id)) {
      workItems.remove(id);
      return true; 

    } else {
      return false; 
    }
  }
}
