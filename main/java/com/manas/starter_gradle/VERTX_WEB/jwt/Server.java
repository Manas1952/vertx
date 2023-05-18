package com.manas.starter_gradle.VERTX_WEB.jwt;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);

    JWTAuthOptions authConfig = new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions().setAlgorithm("HS256").setBuffer("keyboard cat"))
      .setKeyStore(new KeyStoreOptions()
        .setType("jceks")
        .setPath("/home/manas/keystore.jceks")
        .setPassword("secret"));

    JWTAuth jwt = JWTAuth.create(vertx, authConfig);


    router.route("/login").handler(ctx -> {
      // this is an example, authentication should be done with another provider...
      if ("paulo".equals(ctx.request().getParam("username")) && "secret".equals(ctx.request().getParam("password"))) {

        System.out.println("handled");

        String token = ctx.request().headers().get("Authorization").split(" ")[1];

        System.out.println(token);
        jwt.authenticate(new JsonObject()
            .put("token", token)
            .put("options", new JsonObject()
              .put("ignoreExpiration", true)))
          .onSuccess(user -> {
            System.out.println(user.principal());
          })
          .onFailure(result -> {
            System.out.println("failed");
          });

        ctx.response().end(jwt.generateToken(new JsonObject().put("a", "b")));
      } else {

        System.out.println("unauthenticated");
        ctx.fail(401);
      }
    }).failureHandler(result -> {
      System.out.println("failure");
    });

    router.route("/login").handler(JWTAuthHandler.create(jwt));

    vertx.createHttpServer().requestHandler(router).listen(8080);

    startPromise.complete();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server());
  }
}
