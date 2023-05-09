package com.manas.starter_gradle.event_bus;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

class DemoVerticle extends AbstractVerticle{
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("Start Method!");
    vertx.setPeriodic(3000, tid->{
      System.out.println("Hello!");
    });

    startPromise.complete();
  }
}
public class Demo {

  private static Vertx vertx = Vertx.vertx();

  public static void main(String[] args) {
    System.out.println("Hello World!");
    vertx.deployVerticle(DemoVerticle.class.getName());
  }
}
