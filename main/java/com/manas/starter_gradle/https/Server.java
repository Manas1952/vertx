package com.manas.starter_gradle.https;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer(
      new HttpServerOptions()
        .removeEnabledSecureTransportProtocol("TLSv1")
        .removeEnabledSecureTransportProtocol("TLSv1.1")
        .removeEnabledSecureTransportProtocol("TLSv1.2")
        .addEnabledSecureTransportProtocol("TLSv1.3")
        .setSsl(true)
        .setKeyStoreOptions(
          new JksOptions()
            .setPath("/home/manas/keystore.jks")
            .setPassword("storepass"))
            );

    System.out.println(new HttpServerOptions().getSslOptions().getEnabledSecureTransportProtocols() + "...");

    server.requestHandler(req -> {
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>");
    }).listen(8080);
  }
}
