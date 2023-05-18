package com.manas.starter_gradle.VERTX_WEB.sockJS.event_bus_bridge;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);

    EventBus eventBus = vertx.eventBus();

    router.route("/myApp/*")
      .subRouter(sockJSHandler.bridge(new SockJSBridgeOptions()
        .addInboundPermitted(new PermittedOptions().setAddress("hello.consume"))
        .addOutboundPermitted(new PermittedOptions().setAddress("hello.publish"))));

    vertx.setPeriodic(4000, tid -> {
      System.out.println("sent");
      eventBus.publish("hello.publish", "My Name Is Purohit");
    });

    eventBus.consumer("hello.consume", message->{
      System.out.println(message.body());
    });

    vertx.createHttpServer().requestHandler(router).listen(8080);

    startPromise.complete();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server());
  }
}
