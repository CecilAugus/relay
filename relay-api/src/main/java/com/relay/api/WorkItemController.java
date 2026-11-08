package com.relay.api;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.relay.application.WorkItemService;
import com.relay.domain.WorkItem;

import jakarta.validation.Valid;

// @RestController tells Spring to treat this class as a web controller.
// when a method returns a value, Spring sends that values as the response body
// typically encoded as JSON
@RestController 
// This sets the common URL prefix for every endpoint in the class.
// Useful way to group related routes under /work-items
@RequestMapping("/work-items")
public final class WorkItemController {

    private final WorkItemService workItemService; 


    // The controller depends on the service, not directly on the repository
    // That keeps the HTTP layer focuse on HTTP and leaves the app logic in the service
    // and Spring already knows how to create a WorkItemService bean, as it 
    // is already defined in RelayApiApplication
    public WorkItemController(WorkItemService workItemService) {
        this.workItemService = workItemService;
    }

    // Maps HTTP GET requests to /work-items/{id}
    // HTTP GET methods reads data rather than creating or changing it
    // @PathVariable tells Spring to take the {id} portion of the matched URL
    // and pass it into this method. Spring converts the text from URL to a UUID.
    @GetMapping("/{id}")
    public ResponseEntity<WorkItemResponse> findById(@PathVariable("id") UUID id) {
        return this.workItemService.findById(id)
        .map(item -> new WorkItemResponse(
            item.id(),
            item.title(),
            item.creationTime(),
            item.status()))
        .map(ResponseEntity::ok) // return both HTTP status and a response body
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Maps HTTP POST /work-items to this method
    // The class-level @RequestMapping("/work-items") supplies the shared path prefix
    @PostMapping 
    public ResponseEntity<WorkItemResponse> create(
        // Tells Spring to read the incoming HTTP body. Spring's configured JSON
        // message converter (Jackson) parses the JSON and constructs a
        // record CreateWorkItemRequest
        // @Valid tells Spring to check the validation constraints on the converted record
        @Valid @RequestBody CreateWorkItemRequest request
    ) {
        WorkItem item = this.workItemService.create(
            request.title(), 
            Instant.now()); 

        // Builds the URL for the new item from the current request URL, additing id ID.
        // For example, if the request arrived at http://localhost:8080/work-items, 
        // the new location becomes http://localhost:8080/work-items/<id>.
        URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(item.id())
        .toUri();

        WorkItemResponse response = new WorkItemResponse(
            item.id(),
            item.title(),
            item.creationTime(),
            item.status()
        );

        // Creates an HTTP 201 Created response and sets its Location header
        return ResponseEntity.created(location).body(response);
        // .body(response) adds the response record as the body;
        // Spring serializes it to JSON
    }

    // Handles PATCH /work-items/{id}/status
    // The class-level @RequestMapping adds the /work-items prefix
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
        // Take {id} from the URL and convert it to a UUID 
        @PathVariable("id") UUID id,
        // Read the JSON request body and convert it into this record
        @RequestBody ChangeWorkItemStatusRequest request 
    ) {

        // Delegate the operation to the service. It checks the item and applies
        // the domain's allowed status-transition rules          
        this.workItemService.moveTo(id, request.status());

        // 204 means success with no response body
        return ResponseEntity.noContent().build();
    }
}