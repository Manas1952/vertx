package com.manas.starter_gradle.json;

import com.manas.starter_gradle.worker.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WithEventBus {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new Request());
    vertx.deployVerticle(new Response());
  }

  static class Request extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);
    public static final String ADDRESS = "my.request.address";

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();

      final var message = new JsonObject()
        .put("message", "Hello world")
        .put("version", 1);

      LOGGER.debug("Sending {}", message);
      eventBus.<JsonArray>request(ADDRESS, message , reply -> {
        LOGGER.debug("Response {}", reply.result().body());
      });
    }
  }

  static class Response extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<JsonObject>consumer(Request.ADDRESS, message -> {
        LOGGER.debug("Received {}", message.body());
        message.reply(new JsonArray().add("one").add("two").add("three"));
      });
    }
  }
}
