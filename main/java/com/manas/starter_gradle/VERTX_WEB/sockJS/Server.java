package com.manas.starter_gradle.VERTX_WEB.sockJS;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);

    SockJSHandlerOptions options = new SockJSHandlerOptions().setHeartbeatInterval(2000);

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);

    router.route("/myApp/*").subRouter(sockJSHandler.socketHandler(socket -> {
      socket.handler(data -> {
        System.out.println("[Client]: " + data);

        socket.write("[Server]: " + data);
      });
    }));

    vertx.createHttpServer().requestHandler(router).listen(8080);

    startPromise.complete();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server());
  }
}
