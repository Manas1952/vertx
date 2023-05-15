package com.manas.starter_gradle.file_system;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.ext.web.Router;

public class OpenFile {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new MyVerticle1());
  }
}

class MyVerticle1 extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    FileSystem fileSystem = vertx.fileSystem();

    fileSystem.open("/home/manas/Downloads/myFile.txt", new OpenOptions()).onComplete(result -> {
      if (result.succeeded()) {
        AsyncFile asyncFile = result.result();

        asyncFile.write(Buffer.buffer(" --new text-- "), 6);
      } else {
        System.out.println("failed");
      }
    });

    startPromise.complete();
  }
}
