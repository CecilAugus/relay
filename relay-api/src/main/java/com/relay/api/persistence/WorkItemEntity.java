package com.relay.api.persistence;

import java.time.Instant;
import java.util.UUID;

import com.relay.domain.WorkItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @Entity tells JPA that instances of this class are stored as database rows.
@Entity 
// @Table gives the database table an explicit, readable name.
@Table(name = "work_items")
public class WorkItemEntity {

    // The JPA provider uses this field as the rows primary key
    // The domain object already generates the UUID, so the DB does not
    @Id 
    private UUID id; 

    // @Column maps this field to a table column and makes the title mandatory
    @Column(nullable = false)
    private String title; 

    @Column(nullable = false)
    private Instant creationTime;

    // Store enum names such as "IN_PROGRESS", not fragile numeric positions
    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private WorkItem.Status status; 
    
    // JPA needs a no-argument constructor to create an entity while loading a row.
    // protected keeps application code from casually creating empty entities.
    protected WorkItemEntity() {}

     // This constructor copies domain data into the persistence representation.
    public WorkItemEntity(WorkItem workItem) {
        this.id = workItem.id(); 
        this.title = workItem.title();
        this.creationTime = workItem.creationTime();
        this.status = workItem.status();
    }

    public WorkItem convertIntoDomain() {
        return WorkItem.restore(id, title, creationTime, status);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public WorkItem.Status getStatus() {
        return status;
    }
}
