https://medium.com/@limadelrey/vert-x-4-how-to-build-a-reactive-restful-web-service-dd845e3d0b66

# Vert.x 4: How to build a reactive RESTful Web Service
Eclipse Vert.x is a tool-​kit for building reactive applications on the JVM. Reactive applications are both scalable as workloads grow and resilient when failures arise, making them ideal for low-latency and high-throughput use cases. Its main advantage resides in the way it approaches multi-threading. 
Threads can live within a single process, perform concurrent work and share the same memory space. However, as workload grows, the OS kernel struggles when there is too much context switching work with in-flight requests. Because of that, some threads are blocked while they are waiting on I/O operations to complete while others are ready to handle I/O results. Vert.x takes a different approach to this problem by using event loops:

<p align="center"><img width="50%" src="https://cdn-images-1.medium.com/max/720/1*fhQ96tDLMd_zonV_FuVwGA.png"/></p>
<p align="center">Event loop</p>

Instead of blocking a thread when an I/O operation occurs, it moves on to the next event (which is ready to progress) and resumes the initial event when it's the appropriate callback's turn in the queue. As long as the event loop is not blocked, there's a significant performance improvement, making Vert.x one of the fastest in the Java ecosystem.

# What's new in Vert.x 4?
Vert.x is backed by a large ecosystem of reactive modules that are useful to write modern applications: a comprehensive web stack, reactive database drivers, messaging, event streams, clustering, metrics, distributed tracing and more. This release adds a significant number of new features and improvements on top of that:
- Futurisation -  Implementation of future/callback hybrid model;
- Monitoring - Vert.x Tracing supports both Opentracing and Zipkin. It also complements Vert.x Metrics;
- Reactive SQL clients - High-​performance reactive SQL clients, fully integrated with Vert.x Metrics and Vert.x Tracing;
- Reactive Redis client - The revamped client API now supports all Redis connection modes with extensible support for Redis commands;
- SQL Templating - SQL Client Templates is a library designed to facilitate building SQL queries;
- Web Validation - Extensible sync/async HTTP request validator providing a DSL to describe expected HTTP requests;
- Web OpenAPI - New support for Contract Driven development based on OpenAPI;
- Authentication and Authorization - A set of new modules are now available (e.g. vertx-auth-ldap, vertx-auth-sql & vertx-auth-webauth) and improvements were made to older modules (e.g. vertx-auth-oauth).

# Building our reactive RESTful Web Service
There's a good amount of documentation on how to use Vert.x and its modules. However, most of the code examples are focused on a specific feature and presented as a single class Java application (usually MainVerticle). Personally, I feel that it doesn't represent the full reach of its capabilities, so I decided to prepare an application that implements a fully-reactive web service capable of communicating with a relational database (e.g. PostgreSQL) and providing a CRUD API while addressing some production-ready requirements such as metrics, logging, health checks, database migrations, tests and so on. The codebase is organized in traditional N-Tier architecture and it uses some of the latest features such as SQL Templating and Web Validation.
<p align="center"><img width="50%" src="https://cdn-images-1.medium.com/max/720/1*AnNnl6EKV4BjjQlu1Nu94g.png"/></p>
<p align="center">Codebase  package structure</p>

# Router
A router takes an HTTP request, finds the first matching route for that request and proxies the request to that route. Each route can have multiple handlers and each handler should be responsible for a different requirement. You can use this layer to define your API endpoints, your API versioning and even enable request validation. Below you'll find several routes versioned by its URI path using three handlers: a Logger handler, a Web Validation handler and a custom business logic handler.
<p align="center"><img width="50%" src=/></p>
<p align="center"><a href="https://gist.github.com/limadelrey/fb8ea5e7aad27e2ef3f4792ec8655657.js">Router</a></p>

# Handler
As I mentioned before, handlers should be used for different requirements. Vert.x provides many of them out of the box. Logger handlers are used to log requests information, the new Web Validation handlers are used to perform request schema validation, Error handlers are used to customize error messages, the list goes on. You can also implement your own handlers, just like the one that you can find below. This handler provides all the operations necessary by the routes that were defined previously. They extract information from the request (query parameters, path parameters and body) initially and return a JSON response with an appropriate HTTP status code at the end, leaving all the business logic to the service layer.
<p align="center"><a href="https://gist.github.com/limadelrey/fe461ed060c01e527a5f0e2bc26b54e3.js">Handler</a></p>

# Service
Services are a common abstraction to represent middleware. They are used to process data using some business logic and can be very useful to establish a set of available operations that coordinate the application's response. Our service layer is able to create, read, update and delete data. It's responsible for transactional control, for providing connections to the repository layer, for transforming entities into DTOs and for implementing specific features such as pagination. It uses the Reactive PostgreSQL client, which is non-blocking, allowing it to handle many database connections with a single thread.
<p align="center"><a href="https://gist.github.com/limadelrey/2ce8d0dd089746705852cd8c8bced648.js">Service</a></p>

# Repository
Repositories are classes that encapsulate the logic required to access data sources. They centralize common data access functionality, providing better maintainability and decoupling the infrastructure or technology used to access databases from the domain model layer. Below you can find multiple SQL statements as well as the new SQL templating, which can be very useful to map structured data to our entities.
<p align="center"><a href="https://gist.github.com/limadelrey/099dfcdb68b3f7d633108e1cd9a2b47e.js">Repository</a></p>

# Verticle
Vert.x comes with a scalable concurrency model out of the box that you can use to save time writing your own. It shares similarities with the Actor model especially with respect to concurrency, scaling and deployment strategies. Instead of using actors, it uses verticles. There are two types of verticles:
- **Standard** - Standard verticles are assigned an event loop thread when they are created and the start method is called with that event loop. This means you can write all the code in your application as single-threaded and let Vert.x worry about the threading and scaling;
- **Worker** - A worker verticle is just like a standard verticle, but it's executed using a thread from the worker thread pool. They are designed for calling blocking code.

Although the usage of verticles is entirely optional, it's generally a good idea to do it concurrency-wise. Below you'll find a verticle describing how to run an HTTP server with all of your configurations.
<p align="center"><a href="https://gist.github.com/limadelrey/a0157b43ea67694a0cf2c06dd1122972.js">API Verticle</a></p>

# Database migrations
Database management should be composed of incremental and reversible changes allowing us to control our relational database schema versions. By using a schema migration tool (e.g. Flyway) you should have the possibility to define your schema evolution and seed its initial data programmatically. This requirement has a blocking nature, so you should deploy a worker verticle.
<p align="center"><a href="https://gist.github.com/limadelrey/d56098d44b6fc1c2bc6d6040b3faefac.js">Migrations</a></p>
<p align="center"><img width="50%" src="https://cdn-images-1.medium.com/max/720/1*VxvHmbO49j1gbffT5LMSPg.png"/></p>

# Health checks
Health check endpoints enable us to periodically test the health of our service. Sometimes, applications transition to broken states and cannot recover except by being restarted. Other times applications are temporarily unable to serve traffic (e.g. an application might need to load large data during startup) and, in such case, it shouldn't restart the application and/or allow requests either. Providing liveness and readiness probes, respectively,  allows us to detect and mitigate these situations. Below you'll find a way to express the current state of the application and the information on whether a connection to the database can be established.
<p align="center"><a href="https://gist.github.com/limadelrey/df858f15dca397cdfcf0c991fc38f943.js">Health check</a></p>
<p align="center"><img width="50%" src="https://cdn-images-1.medium.com/max/720/1*9fDnwHX0WwbPz3eUx_N9xg.png"/></p>

# Metrics
Application monitoring provides detailed observability into the performance, availability and user experience of applications and their supporting infrastructure. By gathering statistics from the HTTP server, database, API or any existing module you're better prepared to recover from failures and also able to get insights into what is happening inside the application. Vert.x provides a convenient integration with Micrometer whose data can become available through an endpoint that is scraped periodically by Prometheus and consequently by Grafana in order to produce proper dashboards.
<p align="center"><a href="https://gist.github.com/limadelrey/cc90ebfb60def2c6becfe8c845eaf137.js">Metrics</a></p>
<p align="center"><img width="100%" src="https://cdn-images-1.medium.com/max/1080/1*y54dunqhSasDvDidrLsGGA.png"/></p>

# Tests
Finally, it's very important to check whether the actual software matches the expected requirements and/or create an automated way of identifying errors, gaps or missing requirements. There are many types of tests: unit tests, integration tests, component tests, end-to-end tests and so on. Component tests are interesting in the way they allow us to test our web service using the consumer perspective (e.g. API) as the main driver. At the same time, it allows us to test the interaction of the web service with the database, all as one unit. The main challenge is to ensure that the local environment is the same as the production environment. For that reason, you could use Testcontainers in order to mimic PostgreSQL or any other technology.
<p align="center"><a href="https://gist.github.com/limadelrey/cc5b634488b962af8dbf6f53e9d7a3e6.js">Tests</a></p>

# Final thoughts
Modern kernels have very good schedulers, but we cannot expect them to deal with 50k threads as easily as they would do with 5k. It's important to recognize that threads aren't cheap: creating a thread takes a few milliseconds and consumes about 1MB of memory. Vert.x approach addresses these issues and provides a rich ecosystem that allows developers to build highly scalable and performant applications. There's just one caveat: code that runs on event loops should not perform block­ing I/O or lengthy processing. But don't worry if you have such code: Vert.x has worker threads and APIs to process events back on the event loop. You can find the codebase and all the necessary configurations on the following repository.

# Sources
[1] https://vertx.io/  
[2] https://vertx.io/blog/whats-new-in-vert-x-4/  
[3] https://www.techempower.com/benchmarks/