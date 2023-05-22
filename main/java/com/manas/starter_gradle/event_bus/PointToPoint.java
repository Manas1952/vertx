package com.manas.starter_gradle.event_bus;

import com.manas.starter_gradle.worker.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
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

//      vertx.setPeriodic(1000 ,id -> {
      Buffer buffer = Buffer.buffer();
      buffer.appendInt(1);

      vertx.eventBus().send(Sender.class.getName(), "buffer");

      buffer.setInt(0, 2);
      vertx.eventBus().send(Sender.class.getName(), "buffer1");
//      });
      startPromise.complete();
    }
  }

  static class Receiver extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Sender.class.getName(), message -> {
        Buffer buffer = (Buffer) message.body();
        LOGGER.debug("Received {}", buffer.getInt(0));
      });

      vertx.eventBus().consumer(Sender.class.getName(), message -> {
        Buffer buffer = (Buffer) message.body();
        LOGGER.debug("Received {}", buffer.getInt(0));
      });
    }
  }

}
