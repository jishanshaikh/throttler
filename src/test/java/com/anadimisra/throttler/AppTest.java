package com.anadimisra.throttler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {

  private App app;

  @Before
  public void setUp() throws Exception {
    app = new App();
  }

  @Test
  public void testNotRateLimitedOnFirstAccess() throws Exception {
    app.addCustomer("custid", 10, 1);
    assertFalse(app.isRateLimited("custid"));
  }

  @Test
  public void testNotRateLimitedWithinThreshold() throws Exception {
    app.addCustomer("custid", 10, 2);
    app.isRateLimited("custid");
    assertFalse(app.isRateLimited("custid"));
  }

  @Test
  public void testNegativeScenario() throws Exception {
    app.addCustomer("custid", 2, 2);
    app.isRateLimited("custid");
    app.isRateLimited("custid");
    assertTrue(app.isRateLimited("custid"));
  }

  @Test
  public void testRateLimitedOnThreshold() throws Exception {
    app.addCustomer("custid", 1, 2);
    app.isRateLimited("custid");
    assertTrue(app.isRateLimited("custid"));
  }

  @Test
  public void testNotRateLimitedWhenDurationExceeded() throws Exception {
    app.addCustomer("custid", 10, 1);
    app.isRateLimited("custid");
    Thread.sleep(1_500);
    assertFalse(app.isRateLimited("custid"));
  }
}
