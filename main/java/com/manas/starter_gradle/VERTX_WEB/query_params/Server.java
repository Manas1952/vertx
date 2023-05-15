package com.manas.starter_gradle.VERTX_WEB.query_params;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router
      .route(HttpMethod.POST, "/person")
      .handler(context -> {
        System.out.println(context.request().params() + ", " + context.queryParam("age"));
      })
      .failureHandler(context -> {
        System.out.println("error handled");
      });

    // this will not work as already same router is declared
//    router
//      .route(HttpMethod.POST, "/person")
//      .handler(context -> {
//        System.out.println("route 2");
//      });

    server.requestHandler(router).listen(8080);

    startPromise.complete();
  }
}
