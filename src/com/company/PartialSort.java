package com.company;

import java.util.Arrays;
import java.util.Collections;

/**
 * This is an implementation of the PartialSort algorithm which is implemented using
 * the partition selection phase of the quick sort algorithm. Given an array of N elements,
 * this class has the ability to return first top K elements (based on the Comparable implementation)
 * of the array.
 * @param <T> A generic type T which must implement Comparable
 */
public class PartialSort<T extends Comparable<T>> {

  private T[] data;

  public PartialSort(T[] lst) {
    data = lst;
  }

  public void printArray(T[] data) {
    for (int i = 0; i < data.length; i++) {
      System.out.print(data[i] + "\t");
    }
    System.out.print("\n");
  }

  private void swap(T[] lst, int p, int q) {
    T temp = lst[p];
    lst[p] = lst[q];
    lst[q] = temp;
  }

  private void swap(int[] lst, int p, int q) {
    int temp = lst[p];
    lst[p] = lst[q];
    lst[q] = temp;
  }

  public int partition(T[] lst, int start, int end) {
    T x;
    x = lst[start];
    int i = start;
    for (int j = start + 1; j <= end; j++) {
      if (lst[j].compareTo(x) > 0) { // compares lst[j] greater than x
        i = i + 1;
        swap(lst, i, j);
      }
    }
    swap(lst, start, i);
    return i;
  }

  public void quickSort(T[] lst, int start, int end) {
    if (start < end) {
      int index = partition(lst, start, end);
      quickSort(lst, start, index - 1);
      quickSort(lst, index + 1, end);
    }
  }

  public T[] partialSortTopK(int k) {
    T x;
    int index, rank;
    int start = 0;
    int end = data.length - 1;
    while (end > start) {
      x = data[start];
      index = partition(data, start, end);
      rank = index + 1;
      if (rank >= k) {
        end = index - 1;
      } else if ((index - start) > (end - index)) {
        quickSort(data, index + 1, end);
        end = index - 1;
      } else {
        quickSort(data, start, index - 1);
        start = index + 1;
      }
    }
    return data;
  }
}