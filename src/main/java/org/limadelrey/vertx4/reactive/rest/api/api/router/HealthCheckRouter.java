package org.limadelrey.vertx4.reactive.rest.api.api.router;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgPool;

public class HealthCheckRouter {

    private HealthCheckRouter() {

    }

    /**
     * Set health check routes
     *
     * @param vertx    Vertx context
     * @param router   Router
     * @param dbClient PostgreSQL pool
     */
    public static void setRouter(Vertx vertx,
                                 Router router,
                                 PgPool dbClient) {
        final HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);

        healthCheckHandler.register("database",
                promise ->
                        dbClient.getConnection(connection -> {
                            if (connection.failed()) {
                                promise.fail(connection.cause());
                            } else {
                                connection.result().close();
                                promise.complete(Status.OK());
                            }
                        })
        );

        router.get("/health").handler(healthCheckHandler);
    }

}
