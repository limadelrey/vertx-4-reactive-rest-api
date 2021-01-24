package org.limadelrey.vertx4.reactive.rest.api;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.limadelrey.vertx4.reactive.rest.api.verticle.ApiVerticle;
import org.limadelrey.vertx4.reactive.rest.api.verticle.MigrationVerticle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(VertxExtension.class)
public class ComponentTests extends AbstractContainerBaseTest {

    @BeforeAll
    static void setup(Vertx vertx,
                      VertxTestContext testContext) {
        vertx.deployVerticle(new MigrationVerticle(), testContext.succeeding(migrationVerticleId ->
                vertx.deployVerticle(new ApiVerticle(), testContext.succeeding(apiVerticleId ->
                        testContext.completeNow()))));
    }

    @Test
    @Order(1)
    @DisplayName("Read all books")
    void readAll(Vertx vertx, VertxTestContext testContext) {
        final WebClient webClient = WebClient.create(vertx);

        webClient.get(8888, "localhost", "/api/v1/books")
                .as(BodyCodec.jsonObject())
                .send(testContext.succeeding(response -> {
                            testContext.verify(() ->
                                    Assertions.assertAll(
                                            () -> Assertions.assertEquals(200, response.statusCode()),
                                            () -> Assertions.assertEquals(readFileAsJsonObject("src/test/resources/readAll/response.json"), response.body())
                                    )
                            );

                            testContext.completeNow();
                        })
                );
    }

    @Test
    @Order(2)
    @DisplayName("Read one book")
    void readOne(Vertx vertx,
                 VertxTestContext testContext) {
        final WebClient webClient = WebClient.create(vertx);

        webClient.get(8888, "localhost", "/api/v1/books/10")
                .as(BodyCodec.jsonObject())
                .send(testContext.succeeding(response -> {
                            testContext.verify(() ->
                                    Assertions.assertAll(
                                            () -> Assertions.assertEquals(200, response.statusCode()),
                                            () -> Assertions.assertEquals(readFileAsJsonObject("src/test/resources/readOne/response.json"), response.body())
                                    )
                            );

                            testContext.completeNow();
                        })
                );
    }

    @Test
    @Order(3)
    @DisplayName("Create book")
    void create(Vertx vertx,
                VertxTestContext testContext) throws IOException {
        final WebClient webClient = WebClient.create(vertx);
        final JsonObject body = readFileAsJsonObject("src/test/resources/create/request.json");

        webClient.post(8888, "localhost", "/api/v1/books")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(body, testContext.succeeding(response -> {
                            testContext.verify(() ->
                                    Assertions.assertAll(
                                            () -> Assertions.assertEquals(201, response.statusCode()),
                                            () -> Assertions.assertEquals(readFileAsJsonObject("src/test/resources/create/response.json"), response.body())
                                    )
                            );

                            testContext.completeNow();
                        })
                );
    }

    @Test
    @Order(4)
    @DisplayName("Update book")
    void update(Vertx vertx,
                VertxTestContext testContext) throws IOException {
        final WebClient webClient = WebClient.create(vertx);
        final JsonObject body = readFileAsJsonObject("src/test/resources/update/request.json");

        webClient.put(8888, "localhost", "/api/v1/books/37")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(body, testContext.succeeding(response -> {
                            testContext.verify(() ->
                                    Assertions.assertAll(
                                            () -> Assertions.assertEquals(200, response.statusCode()),
                                            () -> Assertions.assertEquals(readFileAsJsonObject("src/test/resources/update/response.json"), response.body())
                                    )
                            );

                            testContext.completeNow();
                        })
                );
    }

    @Test
    @Order(5)
    @DisplayName("Delete book")
    void delete(Vertx vertx,
                VertxTestContext testContext) {
        final WebClient webClient = WebClient.create(vertx);

        webClient.delete(8888, "localhost", "/api/v1/books/23")
                .send(testContext.succeeding(response -> {
                            testContext.verify(() ->
                                    Assertions.assertEquals(204, response.statusCode())
                            );

                            testContext.completeNow();
                        })
                );
    }

    private JsonObject readFileAsJsonObject(String path) throws IOException {
        return new JsonObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
    }

}
