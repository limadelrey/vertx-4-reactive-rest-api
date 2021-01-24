package org.limadelrey.vertx4.reactive.rest.api;

import org.limadelrey.vertx4.reactive.rest.api.utils.ConfigUtils;
import org.testcontainers.containers.GenericContainer;

import java.util.Properties;

abstract class AbstractContainerBaseTest {

    static final GenericContainer POSTGRESQL_CONTAINER;

    static {
        final Properties properties = ConfigUtils.getInstance().getProperties();

        POSTGRESQL_CONTAINER = new GenericContainer<>("postgres:12-alpine")
                .withEnv("POSTGRES_DB", properties.getProperty("datasource.database"))
                .withEnv("POSTGRES_USER", properties.getProperty("datasource.username"))
                .withEnv("POSTGRES_PASSWORD", properties.getProperty("datasource.password"))
                .withExposedPorts(Integer.parseInt(properties.getProperty("datasource.port")));

        POSTGRESQL_CONTAINER.start();

        ConfigUtils.getInstance().getProperties().setProperty("datasource.port", String.valueOf(POSTGRESQL_CONTAINER.getMappedPort(5432)));
    }

}
