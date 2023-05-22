package com.manas.starter_gradle.http.simple;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server(), new DeploymentOptions().setConfig(new JsonObject().put("key", "manas"))). onComplete(handler -> {
      if (handler.succeeded())
      {
        System.out.println("success");
      }
      else
      {
        System.out.println(handler.cause().getMessage());
      }
    });
//    vertx.deployVerticle(new Client());
  }
}
