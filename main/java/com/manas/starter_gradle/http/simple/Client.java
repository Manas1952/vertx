package com.manas.starter_gradle.http.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;

public class Client extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpClient client = vertx.createHttpClient();

    client.request(HttpMethod.GET, 8080, "localhost", "/")
      .compose(req -> req.send()
        .compose(res -> {
          System.out.println("Got response" + res.statusCode());
          return res.body();
        })
        .onSuccess(body -> {
          System.out.println("Got data: " + body.toString("ISO-8859-1"));
        })
        .onFailure(Throwable::printStackTrace)
      );
  }
}
