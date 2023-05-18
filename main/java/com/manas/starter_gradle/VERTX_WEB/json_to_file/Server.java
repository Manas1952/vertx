package com.manas.starter_gradle.VERTX_WEB.json_to_file;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.ext.web.Router;

import java.util.concurrent.atomic.AtomicReference;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);

    AtomicReference<AsyncFile> asyncFile = new AtomicReference<>();

    vertx.fileSystem().open("/home/manas/dummy.txt", new OpenOptions()).onComplete(result -> {
      System.out.println(result.succeeded() + "<-");
      asyncFile.set(result.result());

    });

//    vertx.fileSystem().open("/home/manas/dummy.txt", new OpenOptions()).onComplete(result -> {
//      System.out.println(result.succeeded() + "<-");
//      AsyncFile asyncFile1 = result.result();
//
//      asyncFile1.write(Buffer.buffer(), c -> {
//
//      });
//
//    });

    router.route("/route").handler(context -> {
      System.out.println("handled");
//      AsyncFile asyncFile1 = asyncFile.get();
      context.request().response().setChunked(true);

      System.out.println("route1");
      context.request().pipeTo(asyncFile.get());

      context.response().end("done");
    });

    vertx.createHttpServer().requestHandler(router).listen(8080);

    startPromise.complete();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new Server());
  }
}
