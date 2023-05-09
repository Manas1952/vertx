package com.manas.starter_gradle.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleA extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.debug("start {}", getClass().getName());

    vertx.deployVerticle(new VerticleAA(), whenDeployed -> {
      LOGGER.debug("Deployed {} ", VerticleAA.class.getName());
      vertx.undeploy(whenDeployed.result()); // by deploy ID(whenDeployed.result())
    });

    vertx.deployVerticle(new VerticleAB(), whenDeployed -> {
      LOGGER.debug("Deployed {}", VerticleAB.class.getName());
      // ...
    });

    startPromise.complete();
  }
}
