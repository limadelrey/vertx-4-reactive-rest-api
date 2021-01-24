package org.limadelrey.vertx4.reactive.rest.api.api.handler;

import io.vertx.core.Vertx;
import io.vertx.ext.web.validation.RequestPredicate;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.builder.ParameterProcessorFactory;
import io.vertx.ext.web.validation.builder.Parameters;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;

import static io.vertx.json.schema.common.dsl.Keywords.maxLength;
import static io.vertx.json.schema.common.dsl.Keywords.minLength;
import static io.vertx.json.schema.common.dsl.Schemas.*;
import static io.vertx.json.schema.draft7.dsl.Keywords.maximum;

public class BookValidationHandler {

    private final Vertx vertx;

    public BookValidationHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Build read all books request validation
     *
     * @return ValidationHandler
     */
    public ValidationHandler readAll() {
        final SchemaParser schemaParser = buildSchemaParser();

        return ValidationHandler
                .builder(schemaParser)
                .queryParameter(buildPageQueryParameter())
                .queryParameter(buildLimitQueryParameter())
                .build();
    }

    /**
     * Build read one book request validation
     *
     * @return ValidationHandler
     */
    public ValidationHandler readOne() {
        final SchemaParser schemaParser = buildSchemaParser();

        return ValidationHandler
                .builder(schemaParser)
                .pathParameter(buildIdPathParameter())
                .build();
    }

    /**
     * Build create one book request validation
     *
     * @return ValidationHandler
     */
    public ValidationHandler create() {
        final SchemaParser schemaParser = buildSchemaParser();
        final ObjectSchemaBuilder schemaBuilder = buildBodySchemaBuilder();

        return ValidationHandler
                .builder(schemaParser)
                .predicate(RequestPredicate.BODY_REQUIRED)
                .body(Bodies.json(schemaBuilder))
                .build();
    }

    /**
     * Build update one book request validation
     *
     * @return ValidationHandler
     */
    public ValidationHandler update() {
        final SchemaParser schemaParser = buildSchemaParser();
        final ObjectSchemaBuilder schemaBuilder = buildBodySchemaBuilder();

        return ValidationHandler
                .builder(schemaParser)
                .predicate(RequestPredicate.BODY_REQUIRED)
                .body(Bodies.json(schemaBuilder))
                .pathParameter(buildIdPathParameter())
                .build();
    }

    /**
     * Build delete one book request validation
     *
     * @return ValidationHandler
     */
    public ValidationHandler delete() {
        final SchemaParser schemaParser = buildSchemaParser();

        return ValidationHandler
                .builder(schemaParser)
                .pathParameter(buildIdPathParameter())
                .build();
    }

    private SchemaParser buildSchemaParser() {
        return SchemaParser.createDraft7SchemaParser(SchemaRouter.create(vertx, new SchemaRouterOptions()));
    }

    private ObjectSchemaBuilder buildBodySchemaBuilder() {
        return objectSchema()
                .requiredProperty("author", stringSchema().with(minLength(1)).with(maxLength(255)))
                .requiredProperty("country", stringSchema().with(minLength(1)).with(maxLength(255)).nullable())
                .requiredProperty("image_link", stringSchema().with(minLength(1)).with(maxLength(255)).nullable())
                .requiredProperty("language", stringSchema().with(minLength(1)).with(maxLength(255)).nullable())
                .requiredProperty("link", stringSchema().with(minLength(1)).with(maxLength(255)).nullable())
                .requiredProperty("pages", intSchema().with(maximum(10000)).nullable())
                .requiredProperty("title", stringSchema().with(minLength(1)).with(maxLength(255)))
                .requiredProperty("year", intSchema().with(maximum(10000)).nullable());
    }

    private ParameterProcessorFactory buildIdPathParameter() {
        return Parameters.param("id", intSchema());
    }

    private ParameterProcessorFactory buildPageQueryParameter() {
        return Parameters.optionalParam("page", intSchema());
    }

    private ParameterProcessorFactory buildLimitQueryParameter() {
        return Parameters.optionalParam("limit", intSchema());
    }

}
