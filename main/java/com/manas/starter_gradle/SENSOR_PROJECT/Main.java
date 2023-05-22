package com.manas.starter_gradle.SENSOR_PROJECT;

import com.manas.starter_gradle.SENSOR_PROJECT.temperature_dashboard.DashboardVerticle;
import com.manas.starter_gradle.SENSOR_PROJECT.temperature_sensors.SensorVerticle;
import com.manas.starter_gradle.SENSOR_PROJECT.temperature_store.StoreVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(SensorVerticle.class.getName(), new DeploymentOptions().setInstances(4));

    vertx.deployVerticle(new DashboardVerticle());

    vertx.deployVerticle(new StoreVerticle()).onComplete(result -> {
      System.out.println(result.cause().getMessage());
    });
  }
}
