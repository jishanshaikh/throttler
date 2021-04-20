package com.anadimisra.throttler;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {
  private final Map<String, RateLimit> consumerRateLimits = new HashMap<>();
  private final Map<String, RequestLogData> consumerAccessLogs = new HashMap<>();

  ExecutorService executor = Executors.newFixedThreadPool(5);

  public void addCustomer(String customerId, int numberOfRequests, int durationInSeconds) {
    consumerRateLimits.put(customerId, new RateLimit(numberOfRequests, durationInSeconds));
  }

  public boolean isRateLimited(String customerId) throws Exception {
    Callable<Boolean> task = () -> {
      try {
        RequestLogData accessLog = consumerAccessLogs.get(customerId);

        if (accessLog != null) {
          RateLimit rateLimit = consumerRateLimits.get(customerId);
          Duration duration = Duration.between(Instant.now(), accessLog.getLastAccessTimestamp());

          if( duration.getSeconds() < 1 && accessLog.getRequestCounter()+1 < rateLimit.getNumberOfRequests() )
            return false;
          else
            return true;
        } else {
          consumerAccessLogs.put(customerId, new RequestLogData(1, Instant.now()));

          return false;
        }
      }
      catch (Exception e) {
        return false;
      }
    };

    Future<Boolean> future = executor.submit(task);

    return future.get();
  }
}
