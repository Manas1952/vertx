package com.manas.starter_gradle.http.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    System.out.println(context.config());
    vertx.createHttpServer().requestHandler(request -> {
      request.response().putHeader("content-type", "text/html").end("<html><body><h1>Hi</h1></body></html>");
    }).listen(8080);
  }
}
