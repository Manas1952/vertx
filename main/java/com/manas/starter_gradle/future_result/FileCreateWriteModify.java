package com.manas.starter_gradle.future_result;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

public class FileCreateWriteModify {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MyVerticle());

    HttpClient client = vertx.createHttpClient();
  }

  static class MyVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      FileSystem fileSystem = vertx.fileSystem();

      Future<Void> future = fileSystem
        .createFile("/home/manas/myFile.txt")
        .compose(result -> {
          return fileSystem.writeFile("/home/manas/myFile.txt", Buffer.buffer("This is my text."));
        })
        .compose(result -> {
         return fileSystem.move("/home/manas/myFile.txt", "/home/manas/Downloads/myFile.txt");
        });

      System.out.println(future.result());
    }
  }
}

