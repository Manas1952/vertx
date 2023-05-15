package com.manas.starter_gradle.VERTX_WEB.reroute;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class Server1 extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.get("/route1").handler(context -> {
      context.put("key1", "value1");

      context.next();
    });

    router.get("/route1").handler(context -> {
      context.reroute("/route2");
    });

    router.get("/route2").handler(context -> {
      context.response().end("<h1>HI</h1>");
    });

    server.requestHandler(router).listen(8080);

    startPromise.complete();
  }
}
