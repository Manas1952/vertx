package com.manas.starter_gradle.tcp.send_file;

import io.vertx.core.Vertx;

public class Main {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new Server()).onComplete(handler -> {
      if (handler.succeeded()) {
        System.out.println("Server verticle deployed");
      } else {
        System.out.println("didn't deployed 1");
      }
    });
    vertx.deployVerticle(new Client()).onComplete(handler -> {
      if (handler.succeeded()) {
        System.out.println("Client verticle deployed");
      } else {
        System.out.println("didn't deployed 2");
      }
    });
  }
}
