package com.anadimisra.throttler;

public class RateLimit {

  private final int numberOfRequests;
  private final int durationInSeconds;

  public RateLimit(int numberOfRequests, int durationInSeconds) {
    this.numberOfRequests = numberOfRequests;
    this.durationInSeconds = durationInSeconds;
  }

  public int getNumberOfRequests() {
    return numberOfRequests;
  }

  public int getDurationInSeconds() {
    return durationInSeconds;
  }

}
