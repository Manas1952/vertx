package com.manas.starter_gradle.http.file_upload;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClientResponse;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      if (req.uri().equals("/")) {
        req.response().sendFile("/home/manas/Manas/Vert.x/starter-gradle/src/main/java/com/manas/starter_gradle/http/file_upload/index.html");
      } else if (req.uri().startsWith("/form")) {
        req.setExpectMultipart(true);

        req.uploadHandler(upload -> {
          upload.streamToFileSystem(upload.filename())
            .onSuccess(val -> {
              req.response().end("Uploaded");
            })
            .onFailure(err -> {
              req.response().end("LOL");
            });
        });
      } else {
        req.response().setStatusCode(404).end("E vedya");
      }
    }).listen(8080);
  }
}
