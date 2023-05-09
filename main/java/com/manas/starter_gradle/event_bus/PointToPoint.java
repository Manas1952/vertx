package com.manas.starter_gradle.event_bus;

import com.manas.starter_gradle.worker.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);
  public static final String ADDRESS = "my.request.address";

  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());

  }

  static class Sender extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000 ,id -> {
        vertx.eventBus().send(Sender.class.getName(), "Sending a message");
      });
    }
  }

  static class Receiver extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Sender.class.getName(), message -> {
        LOGGER.debug("Received {}", message.body());
      });
    }
  }

}
