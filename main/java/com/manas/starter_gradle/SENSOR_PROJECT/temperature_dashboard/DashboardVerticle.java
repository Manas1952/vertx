package com.manas.starter_gradle.SENSOR_PROJECT.temperature_dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.slf4j.Logger;

public class DashboardVerticle extends AbstractVerticle {

  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DashboardVerticle.class);
  private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "5000"));

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
    SockJSBridgeOptions bridgeOptions = new SockJSBridgeOptions()
      .addOutboundPermitted(new PermittedOptions().setAddress("temperature.updates"));

    router.route("/eventbus/*").subRouter(sockJSHandler.bridge(bridgeOptions));
    router.route().handler(StaticHandler.create("webroot"));
    router.get("/*").handler(ctx -> ctx.reroute("/index2.html"));

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(httpPort)
      .onSuccess(ok -> {
        logger.info("HTTP server running: http://127.0.0.1:{}", httpPort);
        startPromise.complete();
      })
      .onFailure(startPromise::fail);
  }
}
