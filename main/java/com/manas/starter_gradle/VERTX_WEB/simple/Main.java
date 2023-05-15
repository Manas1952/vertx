package com.manas.starter_gradle.VERTX_WEB.simple;

import io.vertx.core.Vertx;

public class Main {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server());
  }
}
