package com.manas.starter_gradle.VERTX_WEB.context_data;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class Server1 extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println(Thread.currentThread().getName());

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.route().handler(context -> {
      context.put("key", "value");

      context.next();
    });

    router.route().handler(context -> {
      String value = context.get("key");
      String metadata = context.currentRoute().getMetadata("key");

      System.out.println(value);
      System.out.println(Thread.currentThread().getName());
      context.response().end("Hi from server 1");
    });

    server.requestHandler(router).listen(8080).onSuccess(result -> {
      System.out.println("Server 1 is on" + ", " + Thread.currentThread().getName());
    });

    startPromise.complete();
  }
}
