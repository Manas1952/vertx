package com.manas.starter_gradle.VERTX_WEB.sub_route;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router mainRouter = Router.router(vertx);

    Router restAPI = Router.router(vertx);

    restAPI.get("/products/:productID").handler(context -> {
      context.response().end("Your Product");
    });

    restAPI.post("/products").handler(context -> {
      context.response().end("Product Inserted");
    });

    restAPI.delete("/products/:productID").handler(context -> {
      context.response().end("Product Deleted");
    });

    mainRouter.route("/productsAPI/*").subRouter(restAPI);

    server
      .requestHandler(mainRouter)
      .listen(8080)
      .onComplete(result -> {
        if (result.succeeded()) {
          System.out.println("done");
        } else {
          System.out.println("lol");
        }
      });

    startPromise.complete();
  }
}
