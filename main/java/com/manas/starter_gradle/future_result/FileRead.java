package com.manas.starter_gradle.future_result;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;

public class FileRead {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions());

    FileSystem fileSystem = vertx.fileSystem();
    Future<FileProps> future = fileSystem.props("/home/manas/TEST.txt");

    future.onComplete(asyncResult -> {
      if (asyncResult.succeeded()) {
        FileProps props = asyncResult.result();
        System.out.println("file size: " + props.size());
      } else {
        System.out.println("failure, " + asyncResult.cause().getMessage());
      }
    });
    System.out.println("1");

    fileSystem.readFile("/home/manas/TEST.txt", asyncResult -> {
      System.out.println(asyncResult.result());
    });

    System.out.println("2");
  }
}
