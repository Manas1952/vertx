package com.manas.starter_gradle.http.form;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class Server extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      if (req.uri().equals("/")) {
        req.response().sendFile("http/form/inedx.html");

      } else if (req.uri().startsWith("/form")) {

        req.response().setChunked(true);
        req.setExpectMultipart(true);

        req.endHandler(handler -> { // will be called when full data will be received

          for (String attr : req.formAttributes().names()) {

            req.response().write("Got attribute - " + attr + ": " + req.formAttributes().get(attr) + ", ");
          }
          req.response().end();
        });

      } else {
        req.response().setStatusCode(404).end("Get lost");
      }
    }).listen(8080);
  }
}
