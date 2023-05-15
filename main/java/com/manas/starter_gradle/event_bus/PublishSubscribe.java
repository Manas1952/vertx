package com.manas.starter_gradle.event_bus;

import com.manas.starter_gradle.worker.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishSubscribe {
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(Publisher.class.getName() , new DeploymentOptions().setInstances(1)).onComplete(handler -> {
      if (handler.succeeded()) {
        LOGGER.debug("Success");
      } else {
        LOGGER.debug("Fail");
        LOGGER.debug(handler.cause().getMessage());
      }
    });
    vertx.deployVerticle(new Subscriber1());
    vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));
  }
}
