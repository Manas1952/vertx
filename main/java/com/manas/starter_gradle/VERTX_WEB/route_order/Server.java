package com.manas.starter_gradle.VERTX_WEB.route_order;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.route("/route").order(1).handler(context -> {
      HttpServerResponse response = context.response();


      response.write("route1\n");
      context.next();
    });

    router.route("/route").order(0).handler(context -> {
      HttpServerResponse response = context.response();
      response.setChunked(true);

      response.write("route2\n");
      context.next();
    });

    router.route("/route").handler(context -> {
      HttpServerResponse response = context.response();

//      response.write("route3\n");
      context.response().end("end");
    });

    server.requestHandler(router).listen(8080).onComplete(result -> {
      if (result.succeeded()) {
        System.out.println("done");
      } else
        System.out.println("lol");
    });

    startPromise.complete();
  }
}
