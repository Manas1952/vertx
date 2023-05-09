package com.manas.starter_gradle.future_result;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

public class FileCreateWriteModify {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MyVerticle());
  }

  static class MyVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      FileSystem fileSystem = vertx.fileSystem();

      Future<Void> future = fileSystem
        .createFile("/home/manas/myFile.txt")
        .compose(result -> fileSystem.writeFile("/home/manas/myFile.txt", Buffer.buffer("This is my text.")))
        .compose(result -> fileSystem.move("/home/manas/myFile.txt", "/home/manas/Downloads/myFile.txt"));
    }
  }
}

