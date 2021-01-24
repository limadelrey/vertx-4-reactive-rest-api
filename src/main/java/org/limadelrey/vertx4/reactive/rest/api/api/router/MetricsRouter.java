package org.limadelrey.vertx4.reactive.rest.api.api.router;

import io.vertx.ext.web.Router;
import io.vertx.micrometer.PrometheusScrapingHandler;

public class MetricsRouter {

    private MetricsRouter() {

    }

    /**
     * Set metrics routes
     *
     * @param router Router
     */
    public static void setRouter(Router router) {
        router.route("/metrics").handler(PrometheusScrapingHandler.create());
    }

}
