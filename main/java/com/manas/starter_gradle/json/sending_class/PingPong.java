package com.manas.starter_gradle.json.sending_class;

import com.manas.starter_gradle.json.sending_class.Pong.Pong;
import com.manas.starter_gradle.worker.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong extends AbstractVerticle{
    private static final Logger LOGGER = LoggerFactory.getLogger(PingPong.class);
    public static final String ADDRESS = PingVerticle.class.getName();

  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new PingVerticle(), asyncResult -> {
      if (asyncResult.failed()) {
        LOGGER.error("Error: ", asyncResult.cause());
      }
    });
    vertx.deployVerticle(new PongVerticle(), asyncResult -> {
      if (asyncResult.failed()) {
        LOGGER.error("Error: ", asyncResult.cause());
      }
    });
  }

  static class PingVerticle extends AbstractVerticle {


    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();

      final Ping message = new Ping("Hello", true);
      LOGGER.debug("Sending {}", message);

      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));

      eventBus.<Pong>request(ADDRESS, message , reply -> {
        if (reply.failed()) {
          LOGGER.error("Failed {}", reply.cause());
        }
        LOGGER.debug("Response {}", reply.result().body());
      });
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));

      vertx.eventBus().<Ping>consumer(ADDRESS, message -> {
        LOGGER.debug("Received {}", message.body());
        message.reply(new Pong(0));
      }).exceptionHandler(error -> {
        LOGGER.error("Error : ", error);
      });

      startPromise.complete();
    }
  }
}
