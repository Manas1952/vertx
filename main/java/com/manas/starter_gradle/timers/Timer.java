package com.manas.starter_gradle.timers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class Timer {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MyVerticle());
  }
}

class MyVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    long timerID = vertx.setTimer(1000, id -> {
      System.out.println("1 sec done, " + Thread.currentThread().getName());
    });

    System.out.println("Then this, " + Thread.currentThread().getName());

    vertx.cancelTimer(timerID);

    startPromise.complete();
  }
}
