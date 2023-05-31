package com.manas.starter_gradle.VERTX_WEB.json_pojo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.get("/json").respond(context -> {
      return Future.succeededFuture(new JsonObject().put("name", "Manas"));
    });

    router.get("/pojo").respond(context -> {
      context.response().putHeader("content-type", "application/json");
      return Future.succeededFuture(new Pojo());
    });

    server.requestHandler(router).listen(8080);

//    Route route = router.route();

    startPromise.complete();
  }
}
