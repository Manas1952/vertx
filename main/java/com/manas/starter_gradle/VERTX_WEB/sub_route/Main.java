package com.manas.starter_gradle.VERTX_WEB.sub_route;

import io.vertx.core.Vertx;

public class Main {
  public static void main(String[] args) {
    Vertx vetrx = Vertx.vertx();

    vetrx.deployVerticle(new Server());
  }
}
