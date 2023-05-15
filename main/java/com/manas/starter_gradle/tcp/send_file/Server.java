package com.manas.starter_gradle.tcp.send_file;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.net.NetServer;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    NetServer server = vertx.createNetServer();

    server.connectHandler(socket -> {

      socket.handler(buffer -> {
        System.out.println("[Client]: " + buffer.toString());
      });

      socket.sendFile("/home/manas/Downloads/Manas/Audio/bhajan.mpga");
    }).listen(1234);

  }
}
