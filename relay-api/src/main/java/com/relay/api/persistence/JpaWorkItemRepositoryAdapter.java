package com.relay.api.persistence;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.relay.domain.WorkItem;
import com.relay.domain.WorkItemRepository;

// @Repository registers this adapter as a Spring bean.
// It implements the domain's repository interface using JPA underneath.
@Repository
public class JpaWorkItemRepositoryAdapter implements WorkItemRepository {

    private final WorkItemJpaRepository jpaRepository;

    // Spring sees this constructor and supplies the JPA repository bean
    public JpaWorkItemRepositoryAdapter(WorkItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(WorkItem workItem) {

        if (workItem == null) {
            throw new IllegalArgumentException(
                    "workItem can't be null");
        }

        var entity = new WorkItemEntity(workItem);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<WorkItem> findById(UUID id) {
        // JpaRepository.findById returns Optional<WorkItemEntity>.
        // 1. Call jpaRepository.findById(id).
        // 2. Transform the Optional's entity into a domain WorkItem using
        // WorkItemEntity.convertIntoDomain().

        if (id == null) {
            throw new IllegalArgumentException(
                    "id can't be null");
        }

        return jpaRepository.findById(id)
                .map(item -> item.convertIntoDomain());
    }

    @Override
    public Collection<WorkItem> findAll() {
        // JpaRepository.findAll returns a List<WorkItemEntity>.
        // 1. Call jpaRepository.findAll().
        // 2. Stream over the entities and convert each into a WorkItem.
        // 3. Collect the results into a List<WorkItem>.
        return jpaRepository.findAll().stream()
                .map(item -> item.convertIntoDomain())
                .toList();
    }

    @Override
    public boolean remove(UUID id) {
        // Preserve the domain repository's boolean contract:
        // 1. If id is null, return false.
        // 2. Ask JPA whether the ID exists with existsById(id).
        // 3. If absent, return false.
        // 4. Otherwise call deleteById(id), then return true.
        if (id == null) {
            return false;
        }

        if (!jpaRepository.existsById(id)) {
            return false;
        }

        jpaRepository.deleteById(id);
        return true;
    }

}
