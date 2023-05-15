package com.manas.starter_gradle.VERTX_WEB.cookie;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.get().handler(context -> {
//      context.addCookie(Cookie.cookie("startCount", "0"));

      HttpServerRequest request = context.request();

      int visitedCount = Integer.parseInt(request.getCookie("startCount").getValue());

      System.out.println(visitedCount);

      context.addCookie(Cookie.cookie("startCount", String.valueOf(visitedCount + 1)));

      context.response().end(request.getCookie("startCount").getValue());
    });

    server.requestHandler(router).listen(8080);

    startPromise.complete();
  }
}
