package com.manas.starter_gradle;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromise {
  private static final Logger LOGGER = LoggerFactory.getLogger(FuturePromise.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");

    vertx.setTimer(1000, id -> {
      promise.complete("Success");
      LOGGER.debug("Success");
      context.completeNow();
    });

    LOGGER.debug("End");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");

    vertx.setTimer(1000, id -> {
      LOGGER.debug("Timer start");
      promise.complete("Success"); // as soon as promise is completed, it will be handled by future.onSuccess
      LOGGER.debug("Timer done");

    });

    final Future<String> future = promise.future();

    future
      .onSuccess(result -> {
        LOGGER.debug("End");

        context.completeNow();

        LOGGER.debug("completed");
        LOGGER.debug("completed");
        LOGGER.debug("completed");
        LOGGER.debug("completed");

      })
      .onFailure(result -> {
        LOGGER.debug("Result: {}", result);
        context.completeNow();
        context.failNow(new RuntimeException("Forced fail"));
      });
  }

  @Test
  void future_map(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOGGER.debug("Start");

    vertx.setTimer(1000, id -> {
      LOGGER.debug("Timer start");
      promise.complete("Success"); // as soon as promise is completed, it will be handled by future.onSuccess
      LOGGER.debug("Timer done");

    });

    final Future<String> future = promise.future();

    future
      .map(string -> {
        LOGGER.debug("Map string to object");
        return new JsonObject().put("key", string);
      })
      .map(jsonObject -> new JsonArray().add(jsonObject))
      .onSuccess(result -> {
        LOGGER.debug("Result: {}, of type {}", result, result.getClass().getName());

        context.completeNow();

        LOGGER.debug("completed");

      })
      .onFailure(result -> {
        LOGGER.debug("Result: {}", result);
        context.completeNow();
        context.failNow(new RuntimeException("Forced fail"));
      });
  }

  @Test
  void future_coordination(Vertx vertx, VertxTestContext context) {
    vertx.createHttpServer().requestHandler(request -> {
        LOGGER.debug("{}", request);
      })
      .listen(10000)
      .compose(server -> {
        LOGGER.info("Another task");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        LOGGER.info("Even more");
        return Future.succeededFuture(server);
      })
      .onFailure(context::failNow)
      .onSuccess(server -> {
        LOGGER.debug("Server started on port {}", server.actualPort());
      });

    context.completeNow();
  }

  @Test
  void future_composition(Vertx vertx, VertxTestContext context) {
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var three = Promise.<Void>promise();

    var futureOne = one.future().onComplete(result -> {
      if (result.succeeded()) {
        System.out.println("one");
      }
    });
    var futureTwo = two.future().onComplete(result -> {
      if (result.succeeded()) {
        System.out.println("two");
      }
    });
    var futureThree = three.future().onComplete(result -> {
      if (result.succeeded()) {
        System.out.println("three");
      }
    });

    CompositeFuture.all(futureOne, futureTwo, futureThree).onComplete(handler ->{

      if(handler.succeeded())
      {
        System.out.println("sucess all");
      }
      else {
        System.out.println("failed all");
      }
      })
      .onFailure(context::failNow)
      .onSuccess(result -> {
        LOGGER.debug("Success");

        context.completeNow();
      });

    vertx.setTimer(500, id -> {
      one.fail("force fail");
      two.complete();
      three.complete();
    });
  }
}
