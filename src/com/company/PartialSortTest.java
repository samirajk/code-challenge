package com.company;

import org.junit.Assert;
import org.junit.Test;

public class PartialSortTest {
  @Test
  public void testPartialSort() {
    Integer input[] = new Integer[] {0, 1, 9, 5, 4, 8, 7, 11, 3, 15};
    PartialSort<Integer> partialSort = new PartialSort(input);
    Integer output[] = partialSort.partialSortTopK(5);
    Integer expectedOutput[] = new Integer[] {15, 11, 9, 8, 7, 5, 4, 3, 1, 0};
    Assert.assertArrayEquals(output,expectedOutput );
  }
}
