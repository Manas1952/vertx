package com.manas.starter_gradle.event_bus;

import com.manas.starter_gradle.worker.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponse extends AbstractVerticle{

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

      final String message = "Hello world";
      LOGGER.debug("Sending {}", message);
      eventBus.request(ADDRESS, message , reply -> {
        LOGGER.debug("Response {}", reply.result().body());
      });
    }
  }

  static class Response extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Request.ADDRESS, message -> {
        LOGGER.debug("Received {}", message.body());
        message.reply("Received bro");
      });
    }
  }
}
