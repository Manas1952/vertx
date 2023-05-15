package com.manas.starter_gradle.VERTX_WEB.context_data;

import io.vertx.core.Vertx;

public class Main {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server1());
    vertx.deployVerticle(new Server1());
    vertx.deployVerticle(new Server1());
    vertx.deployVerticle(new Server1());
    vertx.deployVerticle(new Server2());
  }
}
