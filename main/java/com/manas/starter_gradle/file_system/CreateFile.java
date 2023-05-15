package com.manas.starter_gradle.file_system;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

public class CreateFile {

  private static Vertx vertx = Vertx.vertx();
  static FileSystem fileSystem = vertx.fileSystem();

  public static void main(String[] args) {

    fileSystem.createFile("foo.txt")
      .compose(result -> {
        return fileSystem.writeFile("foo.txt", Buffer.buffer().appendString("appended text"));
      })
      .compose(result -> {
        return fileSystem.move("foo.txt", "newFoo.txt");
      })
      .onComplete(result -> {
        if (result.succeeded()) {
          System.out.println("done");
        } else {
          System.out.println("failed");
        }
      });

    System.out.println("after updating");
  }
}
