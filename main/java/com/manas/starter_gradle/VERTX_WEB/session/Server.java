package com.manas.starter_gradle.VERTX_WEB.session;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class Server extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    LocalSessionStore sessionStore = LocalSessionStore.create(vertx);

    SessionHandler sessionHandler = SessionHandler.create(sessionStore)
      .setSessionTimeout(1000 * 30)
      .setCookieless(true)
      ;

    router.route().handler(sessionHandler);

    router.route("/").handler(context -> {
      Session session = context.session();

      Integer count = session.get("hitCount");

      System.out.println(count + " of Session: " + session.id());

      count = (count == null ? 0 : count) + 1;

      session.put("hitCount", count);

      context.response().putHeader("content-type", "text/html").end("<html><body><h1>Hitcount: " + count + "</h1><a href='localhost:8080/session/" + session.id() + "' >Go to session</a></body></html>");

    });

    router.route("/session/:sessionID").handler(context -> {
      System.out.println("handled");
      if (context.session().id().equals(context.pathParam("sessionID"))) {
        System.out.println("same session");
      } else {
        System.out.println(context.session().id() + ", " + context.pathParam("sessionID"));
      }
      context.response().end("session");
    });

    server.requestHandler(router).listen(8080);

    startPromise.complete();
  }
}
