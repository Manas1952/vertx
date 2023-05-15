package com.manas.starter_gradle.VERTX_WEB.authentication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.properties.PropertyFileAuthentication;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Server extends AbstractVerticle {
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);

    PropertyFileAuthentication authProvider = PropertyFileAuthentication.create(vertx, "users.properties");

    BasicAuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);

    router.route("/static/*").handler(StaticHandler.create("webroot").setIndexPage("index.html")); // default is 'webroot/index.html'

    router.route("/private/*").handler(basicAuthHandler);
    router.route("/private/path").handler(context -> {

      JsonObject authInfo = new JsonObject()
        .put("username", "tim").put("password", "morris_dancer");

      authProvider.authenticate(authInfo)
        .onSuccess(user -> {
          System.out.println("User " + user.principal() + " is now authenticated");

          context.response().end("private");
        })
        .onFailure(result -> {
          context.response().end("not authenticated");
          System.out.println(result.getMessage());
        });


    });

    vertx.createHttpServer().requestHandler(router).listen(8080);

    startPromise.complete();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server()).onComplete(result -> {
      System.out.println(result.succeeded());
    });
  }
}
