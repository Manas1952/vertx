package com.manas.starter_gradle.tcp.send_file;

import io.netty.buffer.ByteBuf;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
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
            vertx.fileSystem().writeFile("/home/manas/music.mpga", Buffer.buffer((ByteBuf) buffer));

//            System.out.println("[Server]: " + buffer.toString());
          });

//          socket.write("message");


        } else {
          System.out.println("LOL: " + res.cause().getMessage());
        }
      });
  }
}
