package com.anadimisra.throttler;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class App {
  private final Map<String, RateLimit> consumerRateLimits = new HashMap<>();
  private final Map<String, RequestLogData> consumerAccessLogs = new HashMap<>();

  public void addCustomer(String customerId, int numberOfRequests, int durationInSeconds) {
    consumerRateLimits.put(customerId, new RateLimit(numberOfRequests, durationInSeconds));
  }

  public boolean isRateLimited(String customerId) {

    RequestLogData accessLog = consumerAccessLogs.get(customerId);
    if (accessLog != null) {
      RateLimit rateLimit = consumerRateLimits.get(customerId);
      Duration duration = Duration.between(Instant.now(), accessLog.getLastAccessTimestamp());
      if(duration.getSeconds() < 1 && accessLog.getRequestCounter()+1 < rateLimit.getNumberOfRequests())
        return false;
      else
        return true;
    } else {
      consumerAccessLogs.put(customerId, new RequestLogData(1, Instant.now()));
      return false;
    }
  }
}
