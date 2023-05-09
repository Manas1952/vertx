package com.manas.starter_gradle.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample(), new DeploymentOptions()
//      .setWorker(true)
//      .setWorkerPoolSize(1)
      .setWorkerPoolName("my-worker-verticle")
    );
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.debug("Deployed as worker verticle");
    startPromise.complete();
    executeBlockingCode();
    LOGGER.debug("Blocking operation done");
  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> { // will be executed in worker thread
      LOGGER.debug("Execution blocking code");
      try {
        Thread.sleep(5000);
        event.complete();
      } catch (InterruptedException e) {
        LOGGER.error("Failed: ", e);
        event.fail(e);
      }
    }, result -> { // will be executed in event loop thread
      if (result.succeeded()) {
        LOGGER.debug("Blocking call done");
      } else {
        LOGGER.debug("Blocking call failed due to: ", result.cause());
      }
    });
  }
}
