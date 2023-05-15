package com.manas.starter_gradle.json_parser;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.JsonEventType;
import io.vertx.core.parsetools.JsonParser;

import java.util.concurrent.atomic.AtomicInteger;

public class Parser {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MyVerticle());
  }
}


class MyVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    JsonParser parser = JsonParser.newParser();
    AtomicInteger startCount = new AtomicInteger();
    AtomicInteger endCount = new AtomicInteger();
    parser.handler(event -> {
      switch (event.type()) {
        case START_OBJECT:
          System.out.println("START_OBJECT");
          startCount.incrementAndGet();
          break;
        case END_OBJECT:
          System.out.println("END_OBJECT");
          endCount.incrementAndGet();
          break;
        case VALUE:
          System.out.println("VALUE" + event.stringValue());
          break;
        default:
          System.out.println("default");
          break;
      }
    });
    parser.handle(Buffer.buffer("{\"name\": \"Manas\", \"age\": \"21\"}{}"));
    startPromise.complete();
  }
}
