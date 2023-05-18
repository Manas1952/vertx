package com.manas.starter_gradle.future_result;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class Factorial {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new MyVerticle());

    System.out.println("inside main");
  }
}

class MyVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Future<Long> future = factorialFuture(50);

    future.onComplete(handler -> {
      if (handler.succeeded()) {
        System.out.println("Success, " + handler.result());
      } else {
        System.out.println("Fail");
      }
    });

    System.out.println("After future");

    startPromise.complete();
  }

  Future<Long> factorialFuture(int number) throws InterruptedException {
    Promise<Long> promise = Promise.promise();

    long factorial = 1;

    for (int iterator = 2; iterator <= number; iterator++) {
      Thread.sleep(10);
      factorial *= iterator;
    }

    promise.complete(factorial);

    return promise.future();
  }
}
