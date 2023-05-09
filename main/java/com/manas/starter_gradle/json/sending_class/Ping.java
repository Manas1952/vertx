package com.manas.starter_gradle.json.sending_class;

public class Ping {
  private String message;
  private boolean enabled;

  public Ping() {
  }

  public Ping(String message, boolean enabled) {
    this.message = message;
    this.enabled = enabled;
  }

  public String getMessage() {
    return message;
  }

  public boolean isEnabled() {
    return enabled;
  }
}
