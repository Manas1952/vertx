package com.manas.starter_gradle.file_system;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;

public class CopyFile {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new MyVerticle());
  }
}

class MyVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    FileSystem fileSystem = vertx.fileSystem();

    fileSystem.copyBlocking("/home/manas/Downloads/myFile.txt", "/home/manas/myFile.txt");

    fileSystem.copy("/home/manas/Downloads/myFile.txt", "/home/manas/myFile.txt")
        .onComplete(result -> {
          if (result.succeeded()) {
            System.out.println("Done");
          } else {
            System.out.println("Failed");
          }
        });

    System.out.println("after copying");

    startPromise.complete();
  }
}
