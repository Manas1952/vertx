package com.manas.starter_gradle.http.simple;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server(), new DeploymentOptions().setConfig(new JsonObject().put("key", "manas")));
//    vertx.deployVerticle(new Client());
  }
}
