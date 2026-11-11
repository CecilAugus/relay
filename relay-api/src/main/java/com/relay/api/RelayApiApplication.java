package com.relay.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.relay.application.WorkItemService;
import com.relay.domain.WorkItemRepository;

import org.springframework.boot.SpringApplication;

@SpringBootApplication 
public class RelayApiApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RelayApiApplication.class, args);
    }

    @Bean 
    public WorkItemService workItemService(WorkItemRepository repository) {
        return new WorkItemService(repository);
    }

}
