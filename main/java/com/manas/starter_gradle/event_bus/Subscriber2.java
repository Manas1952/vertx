package com.manas.starter_gradle.event_bus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class Subscriber2 extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    System.out.println("Hello In Start");
    vertx.eventBus().<String>consumer("publisher.publish", message -> {
      System.out.println("Received by S2"+ message.body());
    });

    startPromise.complete();
  }
}
