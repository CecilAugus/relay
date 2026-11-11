package com.relay.api.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data detects this interface and creates an implementation at startup.
// JpaRepository supplies common database operations without handwritten SQL.
public interface WorkItemJpaRepository 
    extends JpaRepository<WorkItemEntity, UUID> {} 
// Spring creates methods such as save, findById, and findAll for this interface
