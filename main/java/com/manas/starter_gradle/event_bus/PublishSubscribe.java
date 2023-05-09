package com.manas.starter_gradle.event_bus;

import com.manas.starter_gradle.worker.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

class Subscriber1 extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    System.out.println("Hello In Start");
    vertx.eventBus().<String>consumer(Publisher.class.getName(), message -> {
      System.out.println("Received by S2"+ message.body());
    });

    startPromise.complete();
  }
}
class Publisher extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.setPeriodic(Duration.ofSeconds(3).toMillis(), id -> {
      vertx.eventBus().publish(Publisher.class.getName(), "Message published");
    });

    startPromise.complete();
  }
}
class Subscriber2 extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.eventBus().<String>consumer(Publisher.class.getName(), message -> {
      System.out.println("Received by S2"+ message.body());
    });
    startPromise.complete();
  }
}


public class PublishSubscribe {
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();

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
