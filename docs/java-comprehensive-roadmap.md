# Comprehensive Java and JVM Roadmap

This roadmap assumes you already know basic Java syntax, variables, control flow,
methods, classes, and simple object-oriented programming. Its goal is not merely
to learn Java application development, but to understand the language, the JVM,
production engineering practices, and the ecosystem around them.

Work through the phases in order. For each phase, read, implement, test, debug,
and revisit earlier projects with the new knowledge.

## 0. Fast foundation review

**Goal:** Close gaps before moving into advanced material.

Review:

- The type system: primitives, wrappers, numeric conversion, `String`, arrays,
  enums, records, interfaces, abstract classes, and nested classes.
- Encapsulation, inheritance, composition, polymorphism, and method dispatch.
- Checked versus unchecked exceptions and resource handling with
  try-with-resources.
- Packages, imports, visibility, and Java's module system at a high level.
- Generics basics: parameterized types, wildcards, bounds, and type erasure.
- Lambdas, method references, functional interfaces, streams, and `Optional`.
- Modern Java language features relevant to your installed LTS release.

**Deliverable:** Build a small command-line program that reads structured input,
validates it, persists output to a file, has tests, and uses no framework.

## 1. Core Java fluency

**Goal:** Write clear, idiomatic, maintainable Java without relying on a
framework to hide the language.

Study:

- Collections in depth: `List`, `Set`, `Map`, queues, deques, iterators,
  immutable collections, ordering, equality, hashing, and comparator design.
- Generic API design: bounded type parameters, PECS (`extends` for producers,
  `super` for consumers), generic methods, and heap-pollution hazards.
- I/O and NIO.2: paths, files, buffers, channels, character encodings, streams,
  and asynchronous I/O concepts.
- Date/time: `Instant`, `Duration`, `LocalDateTime`, time zones, parsing, and
  serialization boundaries.
- Regular expressions, internationalization, resource bundles, and Unicode.
- Serialization risks and alternatives such as JSON or protocol buffers.
- Java Platform Module System: module boundaries, encapsulation, services, and
  when not to modularize an application.

**Practice:**

- Implement a CSV/JSON import tool with validation and error reporting.
- Write a generic in-memory repository and query API.
- Build a command-line note or task manager with undo/redo and persistence.

**Key references:** Java API documentation; *Effective Java* by Joshua Bloch.

## 2. Software engineering discipline

**Goal:** Make Java code safe to change and ready to be operated by others.

Study:

- Unit, integration, end-to-end, property-based, mutation, and contract tests.
- JUnit 5, test fixtures, parameterized tests, test doubles, and Testcontainers.
- Maven and Gradle: dependency graphs, scopes/configurations, plugins, test
  lifecycle, reproducible builds, version catalogs, and publishing artifacts.
- Code style, formatting, static analysis, dependency vulnerability scanning,
  and license review.
- Logging with SLF4J and a backend such as Logback; structured logging and
  correlation IDs.
- Documentation: Javadoc, READMEs, architecture decision records, and API
  documentation.
- Git workflows, code review, CI pipelines, artifact repositories, and semantic
  versioning.

**Practice:**

- Convert the Phase 1 project into a multi-module build.
- Add unit and integration tests, code coverage, formatting, static analysis,
  and a CI workflow.
- Publish a small reusable library to a local or hosted package repository.

## 3. Design, APIs, and maintainable architecture

**Goal:** Design software around useful boundaries instead of framework classes.

Study:

- SOLID principles as trade-offs, not rules to apply mechanically.
- Composition over inheritance; domain modeling; value objects; immutability.
- Common patterns: strategy, adapter, decorator, factory, builder, template
  method, observer, command, state, and chain of responsibility.
- Anti-patterns: god objects, anemic models, primitive obsession, temporal
  coupling, shared mutable state, and leaky abstractions.
- Hexagonal/ports-and-adapters architecture, clean architecture, and modular
  monolith design.
- API design: preconditions, nullability, error models, backward compatibility,
  pagination, idempotency, rate limits, and versioning.

**Practice:**

- Build a domain-focused application with independent domain, application,
  infrastructure, and interface modules.
- Write an API that can be used without reading its implementation.
- Refactor one earlier project, documenting the design decisions and trade-offs.

**Key references:** *Effective Java*; *Refactoring* by Martin Fowler;
*Domain-Driven Design* by Eric Evans (selected parts).

## 4. Data structures, algorithms, and complexity

**Goal:** Know the performance behavior of the code and library structures you
choose.

Study:

- Big-O analysis, amortized analysis, memory costs, and algorithmic trade-offs.
- Arrays, linked lists, stacks, queues, heaps, hash tables, trees, tries,
  graphs, union-find, and bloom filters.
- Sorting, binary search, recursion, dynamic programming, greedy algorithms,
  graph traversal, shortest paths, and topological sorting.
- Java collection implementations and their performance characteristics:
  `ArrayList`, `LinkedList`, `HashMap`, `TreeMap`, `ConcurrentHashMap`,
  `PriorityQueue`, and `ArrayDeque`.

**Practice:**

- Implement selected structures yourself, with invariants and tests.
- Profile alternative implementations of the same feature.
- Solve problems where the chosen data structure materially changes latency or
  memory use; do not treat puzzle practice as a substitute for systems work.

## 5. Concurrency and the Java Memory Model

**Goal:** Build correct concurrent systems and understand why incorrect ones
fail.

Study:

- Threads, thread lifecycle, interruption, cancellation, thread pools, and
  executors.
- Race conditions, deadlock, livelock, starvation, thread confinement, and
  immutability.
- `synchronized`, monitors, `volatile`, locks, conditions, atomics, CAS, and
  concurrent collections.
- Happens-before relationships and the Java Memory Model.
- `CompletableFuture`, asynchronous composition, backpressure, and reactive
  streams concepts.
- Virtual threads, structured concurrency, scoped values, and their current
  status in your Java release.
- When parallel streams are appropriate and when they are harmful.

**Practice:**

- Implement a bounded worker pool and graceful shutdown policy.
- Build a concurrent web crawler or file indexer with cancellation, deadlines,
  metrics, and tests for races.
- Diagnose intentionally broken examples with thread dumps and a profiler.

**Key reference:** *Java Concurrency in Practice* by Brian Goetz et al.

## 6. JVM internals and runtime behavior

**Goal:** Understand what happens after Java source is compiled and executed.

Study:

- Compilation from source to bytecode; class files; constant pools; bytecode
  instructions; `javap`.
- Class loading, delegation, linking, initialization, class loaders, module
  boundaries, and classpath versus module path.
- Runtime data areas: stacks, frames, heap, metaspace, native memory, and
  direct buffers.
- JIT compilation, tiered compilation, profiling, deoptimization, inlining,
  escape analysis, scalar replacement, intrinsics, and code cache behavior.
- Garbage collection fundamentals: reachability, generations, pauses,
  throughput, latency, allocation rate, and collectors such as G1, ZGC, and
  Shenandoah where available.
- Java Native Interface, Foreign Function and Memory API, and native-library
  operational risks.
- The Java Language Specification, Java Virtual Machine Specification, Java
  Memory Model, and Java Enhancement Proposals (JEPs).

**Practice:**

- Inspect bytecode for dispatch, lambdas, generics, records, and try-with-
  resources.
- Run controlled allocation experiments under different garbage collectors.
- Explain a GC log and diagnose a deliberately induced memory leak.
- Create a small Java agent or bytecode-inspection tool after learning the
  instrumentation API.

**Tools:** `java`, `javac`, `javap`, `jcmd`, `jstack`, `jmap`, `jstat`, Java
Flight Recorder (JFR), Java Mission Control (JMC), and async-profiler.

**Key references:** *Java Performance* by Scott Oaks; *Optimizing Java* by
Benjamin Evans, James Gough, and Chris Newland.

## 7. Databases and persistence

**Goal:** Correctly model, query, and operate persistent data.

Study:

- Relational modeling, normalization, constraints, transactions, isolation
  levels, locks, indexes, query plans, and connection pooling.
- SQL fluency before ORM abstraction: joins, aggregates, CTEs, window
  functions, pagination, and migrations.
- JDBC: drivers, prepared statements, batches, transactions, result handling,
  resource cleanup, and SQL injection prevention.
- JPA/Hibernate: entity identity, persistence context, mappings, lazy loading,
  fetching, N+1 queries, cascading, optimistic/pessimistic locking, and schema
  migration.
- NoSQL trade-offs: key-value, document, wide-column, and graph databases;
  consistency and data-model choices.

**Practice:**

- Build a service using PostgreSQL and JDBC first.
- Add migrations and integration tests through Testcontainers.
- Rebuild one bounded part with JPA/Hibernate, inspect generated SQL, and
  measure the trade-offs.

## 8. Networking, web services, and backend development

**Goal:** Build robust networked applications and understand their protocols.

Study:

- TCP, TLS, DNS, HTTP/1.1, HTTP/2, HTTP/3 concepts, proxies, reverse proxies,
  load balancing, connection reuse, and timeouts.
- HTTP semantics: methods, status codes, headers, content negotiation, caching,
  conditional requests, cookies, CORS, and multipart uploads.
- REST as a set of constraints, not just JSON over HTTP; RPC and GraphQL
  trade-offs.
- JSON parsing and serialization, schema evolution, validation, error
  responses, pagination, filtering, and idempotency keys.
- Servlet APIs and an embedded server before depending heavily on a framework.
- Framework internals and practice with Spring Boot: dependency injection,
  configuration, MVC/WebFlux choices, validation, data access, security,
  actuator, and testing.
- Netty concepts for event-driven networking and high-throughput servers.

**Practice:**

- Implement a small HTTP server using the JDK or a low-level library.
- Build a documented REST API with authentication, PostgreSQL, validation,
  rate limiting, structured errors, metrics, and integration tests.
- Trace a request end-to-end through application, database, and logs.

## 9. Security engineering

**Goal:** Make security a design constraint rather than an afterthought.

Study:

- Threat modeling, trust boundaries, least privilege, secure defaults, and
  secrets management.
- Authentication: passwords, hashing, MFA concepts, sessions, tokens, OAuth
  2.0, OpenID Connect, and authorization models.
- OWASP risks: injection, broken access control, insecure deserialization,
  SSRF, XXE, path traversal, insecure file upload, and dependency risks.
- Cryptography usage: secure random values, keys, signatures, encryption at
  rest/in transit, certificate validation, and why custom cryptography is
  unsafe.
- Java-specific risks: deserialization, reflection, dynamic class loading,
  dependency confusion, XML parsers, and unsafe logging.

**Practice:**

- Add role-based authorization and audit logging to an API.
- Threat-model it, then create security-focused tests for important boundaries.
- Configure dependency scanning and remediate a controlled vulnerable
  dependency exercise.

## 10. Distributed systems and asynchronous architectures

**Goal:** Design services that remain correct under partial failure.

Study:

- Failure modes: timeouts, retries, duplicate delivery, ordering, partitions,
  clock skew, and cascading failure.
- CAP-related trade-offs, consistency models, replication, sharding, and
  leader election concepts.
- Message brokers and event streaming: queues, topics, consumer groups,
  offsets, dead-letter queues, backpressure, and schema compatibility.
- Idempotency, outbox/inbox patterns, sagas, eventual consistency, and
  compensating actions.
- Caching: cache-aside, write-through, invalidation, stampedes, TTLs, and
  consistency trade-offs.
- Resilience patterns: circuit breakers, bulkheads, rate limiting, load
  shedding, and bounded retries with jitter.

**Practice:**

- Split a modest domain into two services only after a modular-monolith version
  exists.
- Use a broker to publish events with an outbox pattern.
- Test duplicate messages, retries, unavailable dependencies, and slow
  consumers.

## 11. Performance, observability, and reliability

**Goal:** Measure systems accurately and improve the actual bottleneck.

Study:

- Latency versus throughput, tail latency, saturation, queueing effects, and
  capacity planning.
- Benchmark design, warmup, dead-code elimination, JMH, representative data,
  and avoiding misleading microbenchmarks.
- Profiling CPU, allocation, locks, I/O, and native memory.
- Metrics, logs, traces, correlation IDs, SLIs, SLOs, alerting, dashboards,
  incident response, and postmortems.
- Load testing, failure injection, chaos engineering principles, and
  operational runbooks.

**Practice:**

- Benchmark and profile a real endpoint before and after a targeted change.
- Export metrics and traces using Micrometer and OpenTelemetry.
- Define SLOs for a service, simulate a dependency outage, and document the
  investigation and remediation.

## 12. Delivery, operations, and cloud-native Java

**Goal:** Ship Java services reliably.

Study:

- Linux processes, signals, filesystems, environment configuration, and network
  diagnostics.
- Containers: image construction, layers, non-root users, JVM container
  awareness, startup time, memory limits, and image security.
- CI/CD: build caching, test stages, artifact promotion, deployments, rollbacks,
  and supply-chain integrity.
- Kubernetes concepts: deployments, services, ingress, configuration, secrets,
  health checks, resource requests/limits, autoscaling, and observability.
- Cloud fundamentals: managed databases, object storage, queues, IAM, networks,
  cost controls, and disaster recovery.

**Practice:**

- Containerize the backend service with a minimal, reproducible image.
- Deploy it with a database and observability stack to a local Kubernetes
  environment or a small cloud environment.
- Exercise rollback, backup restoration, and configuration rotation.

## 13. Framework, library, and open-source literacy

**Goal:** Avoid treating dependencies as magic.

Study by reading source and debugging through:

- JDK collections, streams, concurrency utilities, HTTP client, and NIO.
- Spring Framework/Spring Boot startup, bean resolution, web request handling,
  transactions, and configuration.
- Hibernate SQL generation and persistence-context behavior.
- Netty event loops and pipeline model.
- Jackson serialization behavior and security options.
- Logging and observability libraries.

**Practice:**

- Debug through framework code from a breakpoint in your application.
- Read relevant release notes and JEPs before upgrading an LTS release.
- Submit a documentation, test, or small bug fix to a Java ecosystem project.

## 14. Capstone: a production-quality Java system

Build one application that forces you to integrate the roadmap. A suitable
example is a multi-user scheduling, inventory, billing, or workflow service.

It should include:

- A modular domain model and documented architecture.
- A versioned HTTP API with validation and consistent errors.
- Authentication, authorization, audit events, and secure configuration.
- PostgreSQL with migrations, transactions, indexes, and integration tests.
- An asynchronous workflow through a message broker, with idempotent handlers.
- Caching only where measured or clearly justified.
- Unit, integration, and end-to-end tests; CI quality gates.
- Structured logs, metrics, traces, dashboards, and defined SLOs.
- Container deployment, health checks, resource limits, and a rollback plan.
- Load testing and profiling, including a written performance investigation.
- A threat model, operational runbook, and postmortem for an intentional
  failure scenario.

## Suggested study cadence

Use a loop rather than attempting to finish every book before programming:

1. Study one focused topic.
2. Implement a small, testable feature that uses it.
3. Break it deliberately or inspect it with tools.
4. Read the relevant API documentation, source code, specification, or JEP.
5. Write down what the abstraction guarantees, its costs, and its failure modes.
6. Apply the lesson to the capstone or an earlier project.

For sustained progress, reserve most study time for implementation, debugging,
testing, profiling, and reading real source code. Certificates can demonstrate
completion, but deployed projects, clear technical writing, and the ability to
diagnose behavior are stronger evidence of comprehensive Java engineering skill.
