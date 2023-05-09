package com.manas.starter_gradle.event_loops;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.time.Duration;

 class Publisher extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.setPeriodic(Duration.ofSeconds(3).toMillis(), id -> {
      vertx.eventBus().publish(Publisher.class.getName(), "Message published");
    });

    startPromise.complete();
  }
}
