package com.manas.starter_gradle.http.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CSRFHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println(Context.isOnEventLoopThread());
    System.out.println(Context.isOnVertxThread());
    System.out.println(Context.isOnWorkerThread());

    System.out.println(context.config());

    Router router = Router.router(vertx);

    router.route().handler(CSRFHandler.create(vertx, "abracadabra"));
    router.route().handler(CorsHandler.create());

    router.route("/route").handler(context -> {
      context.response().end("1");
    });

    router.route("/route").handler(context -> {
      context.response().end("2");
    });
    router.route().handler(context ->  {
      context.response().putHeader("content-type", "text/html").end("<html><body><h1>Hi</h1></body></html>");
    });


    vertx.createHttpServer().requestHandler(router).listen(8080, result -> {
      System.out.println(result.succeeded());
      startPromise.complete();
    });

    System.out.println(1);
  }
}
