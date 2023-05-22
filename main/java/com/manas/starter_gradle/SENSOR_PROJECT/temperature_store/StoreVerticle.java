package com.manas.starter_gradle.SENSOR_PROJECT.temperature_store;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class StoreVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(StoreVerticle.class);
  private static final int httpPort = 7000;

  private SqlClient client;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    client = PgPool.client(vertx, new PgConnectOptions()
        .setHost("127.0.0.1")
        .setDatabase("postgres")
        .setUser("postgres")
        .setPassword("postgres")
      , new PoolOptions());

    vertx.eventBus().<JsonObject>consumer("temperature.updates", this::recordTemperature);

    Router router = Router.router(vertx);

    router.get("/last-5-minutes").handler(this::getLastFiveMinutes);
    router.get("/get").handler(this::getData);
    router.get("/getAllData").handler(this::getAllData);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(7000)
      .onSuccess(ok -> {
      });
  }

  private void recordTemperature(Message<JsonObject> message) {
    JsonObject body = message.body();

    String uuid = body.getString("uuid");
    OffsetDateTime timestamp = OffsetDateTime.ofInstant(Instant.ofEpochMilli(body.getLong("timestamp")), ZoneId.systemDefault());
    Double temperature = body.getDouble("temperature");

    System.out.println(body);

    String query = "insert into temperature_records(uuid, tstamp, value) values ($1, $2, $3);";

    Tuple tuple = Tuple.of(uuid, timestamp, temperature);

    client.preparedQuery(query)
      .execute(tuple)
      .onSuccess(row -> LOGGER.info("Recorded: {}", tuple.deepToString()))
      .onFailure(failure -> LOGGER.error("Recording failed", failure));
  }

  private void getLastFiveMinutes(RoutingContext context) {
    String query = "select * from temperature_records where tstamp >= now() - interval '5 minutes'";

    client.preparedQuery(query)
      .execute()
      .onSuccess(rows -> {
        JsonArray data = new JsonArray();
        for (Row row : rows) {
          data.add(new JsonObject()
            .put("uuid", row.getValue("uuid"))
            .put("timestamp", row.getValue("tstamp").toString())
            .put("value", row.getValue("value")));
        }

        context.response()
          .putHeader("Content-Type", "application/json")
          .end(new JsonObject().put("data", data).encode());
      })
      .onFailure(failure -> {
        LOGGER.error("Query faliled", failure);
        context.fail(500);
      });
  }

  private void getData(RoutingContext context) {
    String uuid = context.request().getParam("uuid");
    String query = "select * from temperature_records where uuid='" + uuid + "';";

    client.query(query)
      .execute()
      .onSuccess(rows -> {
        JsonArray data = new JsonArray();
        for (Row row : rows) {
          data.add(new JsonObject()
            .put("timestamp", row.getValue("tstamp").toString())
            .put("value", row.getValue("value")));
        }

        context.response()
          .putHeader("Content-Type", "application/json")
          .end(new JsonObject().put("data", data).encode());
      })
      .onFailure(failure -> {
        LOGGER.error("Query failed", failure);
        context.fail(500);
      });
  }

  private void getAllData(RoutingContext context) {
    String query = "select * from temperature_records";

    client.query(query)
      .execute()
      .onSuccess(rows -> {
        JsonArray data = new JsonArray();

        for (Row row : rows) {
          data.add(new JsonObject()
            .put("uuid", row.getValue("uuid"))
            .put("timestamp", row.getValue("tstamp").toString())
            .put("value", row.getValue("value")));
        }

        context.response()
          .putHeader("Content-type", "application/json")
          .end(new JsonObject().put("data", data).encode());
      })
      .onFailure(failure -> {
        LOGGER.error("Query failed", failure);
        context.fail(500);
      });
  }
}
