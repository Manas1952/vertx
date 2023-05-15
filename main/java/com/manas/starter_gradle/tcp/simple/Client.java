package com.manas.starter_gradle.tcp.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

public class Client extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    NetClient client = vertx.createNetClient();

    client
      .connect(1234, "localhost")
      .onComplete(res -> {
        if (res.succeeded()) {
          System.out.println("Connected");
          NetSocket socket = res.result();

          socket.handler(buffer -> {
            System.out.println("[Server]: " + buffer.toString());
          });

          for (int iterator = 0; iterator < 5; iterator++) {
            try {
              Thread.sleep(1);
            } catch (InterruptedException e) {
              System.out.println(e.getMessage());
            }
            socket.write("message" + iterator);
          }

        } else {
          System.out.println("LOL: " + res.cause().getMessage());
        }
      });
  }
}
