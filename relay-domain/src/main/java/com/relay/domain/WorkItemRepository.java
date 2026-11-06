package com.relay.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface WorkItemRepository {

  public void save(WorkItem workItem);

  public Optional<WorkItem> findById(UUID id);

  public Collection<WorkItem> findAll();
}
