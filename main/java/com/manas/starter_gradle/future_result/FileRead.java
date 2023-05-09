package com.manas.starter_gradle.future_result;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;

public class FileRead {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

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
  }
}
