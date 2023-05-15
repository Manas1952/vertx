package com.manas.starter_gradle.VERTX_WEB.reroute;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class Server2 extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.get("/my-pretty-notfound-handler").handler(ctx -> {
      System.out.println("**");
      ctx.response()
        .setStatusCode(404)
        .end("NOT FOUND fancy html here!!!");
    });

    router.get("/manas").handler(ctx->{
      System.out.println("Handled!");
      throw  new RuntimeException();
//      ctx.end("Hello S");
    }).failureHandler(ctx -> {
      System.out.println("*");
      if (ctx.statusCode() == 500) {
        ctx.reroute("/my-pretty-notfound-handler");
      } else {
        ctx.next();
      }
    });

//    router.errorHandler(405,handler->{
//      System.out.println("*****");
//    });

    server.requestHandler(router).listen(8080);

    startPromise.complete();
  }
}
