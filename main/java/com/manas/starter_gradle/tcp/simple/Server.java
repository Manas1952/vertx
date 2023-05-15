package com.manas.starter_gradle.tcp.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.net.NetServer;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    NetServer server = vertx.createNetServer();

    server.connectHandler(socket -> {

      socket.setWriteQueueMaxSize(1);

      socket.handler(buffer -> {
        System.out.println("[Client]: " + buffer.toString());
      });

      for (int iterator = 10; iterator < 15; iterator++) {
//        try {
//          Thread.sleep(1);
//        } catch (InterruptedException e) {
//          System.out.println(e.getMessage());
//        }
        socket.write("message" + iterator);
      }
    }).listen(1234);

  }
}
