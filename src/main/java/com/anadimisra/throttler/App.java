package com.anadimisra.throttler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
  private final Map<String, RateLimit> consumerRateLimits = new HashMap<>();
  private final Map<String, RequestLogData> consumerAccessLogs = new HashMap<>();

  public void addCustomer(String customerId, int numberOfRequests, int durationInSeconds) {
    consumerRateLimits.put(customerId, new RateLimit(numberOfRequests, durationInSeconds));
  }

  public boolean useQueueRateLimited(String customerId) throws Exception {
    boolean isRateLimited = isRateLimited(customerId);

    if ( isRateLimited ) {
      AppQueue appQueue = new AppQueue();

      Runnable task = () -> {
        try {
          isRateLimited(customerId);
        } catch (Exception e) {
          e.printStackTrace();
        }
      };

      RateLimit rateLimit = consumerRateLimits.get(customerId);

      RequestLogData accessLog = consumerAccessLogs.get(customerId);
      Duration duration = Duration.between(Instant.now(), accessLog.getLastAccessTimestamp());

      appQueue.sheduleTask(task, 2, rateLimit.getDurationInSeconds() - duration.getSeconds());
    }

    return isRateLimited;
  }

  public boolean useFlux(String customerId) throws Exception {
    Flux<String> flux = Flux.just(customerId);

    Mono<List<Boolean>> fluxData = flux.map(cid -> {
      return isRateLimited(cid);
    } ).collectList();

    List<Boolean> actualData = fluxData.block();

    return actualData.get(0);
  }

  public boolean isRateLimited(String customerId) {
    RequestLogData accessLog = consumerAccessLogs.get(customerId);

    if ( accessLog != null ) {
      RateLimit rateLimit = consumerRateLimits.get(customerId);
      Duration duration = Duration.between(Instant.now(), accessLog.getLastAccessTimestamp());

      if ( duration.getSeconds() < 1 && accessLog.getRequestCounter() + 1 < rateLimit.getNumberOfRequests() )
        return false;
      else
        return true;
    } else {
      consumerAccessLogs.put(customerId, new RequestLogData(1, Instant.now()));

      return false;
    }
  }
}
