package com.anadimisra.throttler;

import java.time.Instant;

public class RequestLogData {

  private final int requestCounter;
  private final Instant lastAccessTimestamp;

  public RequestLogData(int requestCounter, Instant lastAccessTimestamp) {
    this.requestCounter = requestCounter;
    this.lastAccessTimestamp = lastAccessTimestamp;
  }

  public int getRequestCounter() {
    return requestCounter;
  }

  public Instant getLastAccessTimestamp() {
    return lastAccessTimestamp;
  }
}
