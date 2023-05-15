package com.manas.starter_gradle.event_bus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.time.Duration;

public class Publisher extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.setPeriodic(Duration.ofSeconds(3).toMillis(), id -> {
      vertx.eventBus().publish("publisher.publish", "Message published");
    });

    startPromise.complete();
  }
}
