package com.relay.api;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.relay.application.WorkItemService;
import com.relay.application.WorkItemNotFoundException;
import com.relay.domain.InvalidWorkItemTransitionException;
import com.relay.domain.WorkItem;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Load Spring MVC and this controller without starting the real server or
// loading the full application and repository.
@WebMvcTest(WorkItemController.class)
class WorkItemControllerTest {

    // MockMvc sends simulated HTTP requests through Spring's MVC pipeline.
    // This exercises routing, request conversion, controller invocation,
    // response status/header handling, and JSON response conversion.
    @Autowired
    private MockMvc mockMvc;

    // Replace the real service bean with a Mockito mock in this test context.
    // Each test can define what the controller's service call returns.
    @MockitoBean
    private WorkItemService workItemService;

    @Test
    void createReturns201LocationAndCreatedItem() throws Exception {
        // Arrange: make a predictable domain item to return from the service.
        WorkItem item = new WorkItem(
                "Learn Spring",
                Instant.parse("2026-11-08T10:00:00Z"));

        // The controller supplies the title from JSON and the current time.
        // Match the exact title and accept any Instant for this test.
        when(workItemService.create(eq("Learn Spring"), any(Instant.class)))
                .thenReturn(item);

        // Act: send a simulated POST with the JSON body a client would send.
        // Assert: Spring should respond with 201, a Location header, and JSON.
        mockMvc.perform(post("/work-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Learn Spring\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        containsString("/work-items/" + item.id())))
                .andExpect(jsonPath("$.id").value(item.id().toString()))
                .andExpect(jsonPath("$.title").value("Learn Spring"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void createReturns400WhenTitleIsBlank() throws Exception {
        // Act + assert: @NotBlank should reject a whitespace-only title before
        // the controller calls the application service.
        mockMvc.perform(post("/work-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"   \"}"))
                .andExpect(status().isBadRequest());

        verify(workItemService, never()).create(any(), any());
    }

    @Test
    void createReturns400WhenTitleIsMissing() throws Exception {
        // With no title property, JSON conversion creates a request whose title
        // is null. @NotBlank should reject it before the service is called.
        mockMvc.perform(post("/work-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(workItemService, never()).create(any(), any());
    }

    @Test
    void findByIdReturns200AndItemWhenFound() throws Exception {
        // Arrange: have the mock service report that this item exists.
        WorkItem item = new WorkItem(
                "Learn Spring",
                Instant.parse("2026-11-08T10:00:00Z"));

        when(workItemService.findById(item.id()))
                .thenReturn(Optional.of(item));

        // Act + assert: request the item's URL and check the successful response.
        mockMvc.perform(get("/work-items/{id}", item.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.id().toString()))
                .andExpect(jsonPath("$.title").value("Learn Spring"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void findByIdReturns404WhenItemDoesNotExist() throws Exception {
        // Arrange: tell the mock service that this generated ID is absent.
        UUID missingId = UUID.randomUUID();
        when(workItemService.findById(missingId))
                .thenReturn(Optional.empty());

        // Act + assert: a missing item should be an HTTP 404 response.
        mockMvc.perform(get("/work-items/{id}", missingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void changeStatusReturns204AndPassesIdAndStatusToService() throws Exception {
        // Arrange: create an ID that will be placed in the request URL.
        UUID id = UUID.randomUUID();

        // Act: send PATCH with the status in JSON; assert success and an empty body.
        mockMvc.perform(patch("/work-items/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        // Verify Spring/controller converted both inputs correctly and delegated
        // the change to the service.
        verify(workItemService).moveTo(id, WorkItem.Status.IN_PROGRESS);
    }

    @Test
    void changeStatusReturns404WhenWorkItemDoesNotExist() throws Exception {
        // Arrange: make the service report that this requested ID was not found.
        UUID missingId = UUID.randomUUID();
        doThrow(new WorkItemNotFoundException("Item not found"))
                .when(workItemService)
                .moveTo(missingId, WorkItem.Status.IN_PROGRESS);

        // Act + assert: Spring's exception advice should translate the exception
        // into HTTP 404 rather than exposing it as an internal server error.
        mockMvc.perform(patch("/work-items/{id}/status", missingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void changeStatusReturns409WhenTransitionIsInvalid() throws Exception {
        // Arrange: make the service report a forbidden status transition.
        UUID id = UUID.randomUUID();
        doThrow(new InvalidWorkItemTransitionException("Transition not allowed"))
                .when(workItemService)
                .moveTo(id, WorkItem.Status.DONE);

        // Act + assert: Spring's exception advice should translate this
        // state conflict into HTTP 409 Conflict.
        mockMvc.perform(patch("/work-items/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DONE\"}"))
                .andExpect(status().isConflict());
    }
}
