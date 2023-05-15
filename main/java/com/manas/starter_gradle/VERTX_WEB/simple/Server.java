package com.manas.starter_gradle.VERTX_WEB.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);
    Route route = router.route("/manas");

    route.handler(context -> {
      HttpServerResponse response = context.response();
      response.setChunked(true);

      response.write("Hello Client by route1\n");
      context.vertx().setTimer(1000, tid -> {
        System.out.println(1);
        context.next();
      });
    });

    route.handler(context -> {
      HttpServerResponse response = context.response();

      response.write("Hello Client by route2\n");
      context.vertx().setTimer(1000, tid -> {
        System.out.println(2);
        context.next();
      });
    });

    route.handler(context -> {
      HttpServerResponse response = context.response();

      response.write("Hello Client by route3\n");
      context.vertx().setTimer(1000, tid -> {
        System.out.println(3);
        context.response().end("Hi");
      });
    });

    server.requestHandler(router).listen(8080);

    startPromise.complete();
  }
}
