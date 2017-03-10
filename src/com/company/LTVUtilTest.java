package com.company;

import org.junit.Assert;
import org.junit.Test;

public class LTVUtilTest {
  @Test
  public void testGetWeekYearKey() throws Exception {
    Assert.assertEquals(LTVUtil.getWeekYearKey("2017-01-01T12:46:46.384Z"), "2017_1");
    Assert.assertEquals(LTVUtil.getWeekYearKey("2017-01-02T12:46:46.384Z"), "2017_1");
    Assert.assertEquals(LTVUtil.getWeekYearKey("2017-03-09T12:46:46.384Z"), "2017_10");
  }
}
