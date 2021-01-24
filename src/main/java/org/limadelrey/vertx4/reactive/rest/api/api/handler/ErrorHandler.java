package org.limadelrey.vertx4.reactive.rest.api.api.handler;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestPredicateException;
import org.limadelrey.vertx4.reactive.rest.api.utils.ResponseUtils;

public class ErrorHandler {

    private ErrorHandler() {

    }

    /**
     * Build error handler
     * It's useful to handle errors thrown by Web Validation
     *
     * @param router Router
     */
    public static void buildHandler(Router router) {
        router.errorHandler(400, rc -> {
            if (rc.failure() instanceof BadRequestException) {
                if (rc.failure() instanceof ParameterProcessorException) {
                    // Something went wrong while parsing/validating a parameter
                    ResponseUtils.buildErrorResponse(rc, new IllegalArgumentException("Path parameter is invalid"));
                } else if (rc.failure() instanceof BodyProcessorException | rc.failure() instanceof RequestPredicateException) {
                    // Something went wrong while parsing/validating the body
                    ResponseUtils.buildErrorResponse(rc, new IllegalArgumentException("Request body is invalid"));
                }
            }
        });
    }

}
