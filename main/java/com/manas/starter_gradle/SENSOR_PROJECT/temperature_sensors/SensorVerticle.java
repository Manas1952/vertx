package com.manas.starter_gradle.SENSOR_PROJECT.temperature_sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

public class SensorVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(SensorVerticle.class);
  private static final int port = 8080;
  private final String uuid = UUID.randomUUID().toString();
  private double temperaure = 21.0;
  private final Random random = new Random();

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.setPeriodic(2000, this::updateTemperature);

    Router router = Router.router(vertx);
    router.route("/data").handler(this::getData);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(port)
      .onSuccess(result -> {
        LOGGER.info("Server is up on the port: {}", port);
      })
      .onFailure(startPromise::fail);

    startPromise.complete();
  }

  private void getData(RoutingContext context) {
    LOGGER.info("Processing HTTP request from {}", context.request().remoteAddress());

    context.response()
      .putHeader("Content-Type", "application/json")
      .setStatusCode(200)
      .end(createPayload().encode());
  }

  private JsonObject createPayload() {
    return new JsonObject()
      .put("uuid", uuid)
      .put("temperature", temperaure)
      .put("timestamp", System.currentTimeMillis());
  }

  private void updateTemperature(Long id) {
    temperaure = temperaure + (random.nextGaussian() / 2.0d);
    LOGGER.info("Temperature updated: {}", temperaure);

    vertx.eventBus().publish("temperature.updates", createPayload());
  }
}
