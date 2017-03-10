package com.company;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This is the entity that maintains information about a customer, maintaining statistics like
 * number of site visits, the total amount spent, and a record of weeks with data. This also has logic
 * to consume events and update the above data structures, as well as keep the ltv attribute up-to-date.
 */
public class Customer implements Comparable<Customer> {
  private String id;
  private long numSiteVisits;
  private Set<String> weekYearKeys;
  private double totalAmountSpent;
  private double ltv;

  public Customer(String id) {
    this.id = id;
    this.weekYearKeys = new HashSet<String>();
  }

  /**
   * This consumes a give event and makes appropriate updates to the internal data structures
   * @param event The Event to be consumed
   */
  public void consumeEvent(Event event) {
    if (event.getEventType() == Event.EventType.SITE_VISIT) {
      incNumSiteVisits(event.getWeekYearKey());
    } else if (event.getEventType() == Event.EventType.ORDER) {
      addToTotalAmountSpent(((OrderEvent) event).totalAmount);
    }
  }

  /**
   * This updates the LTV metric for the customer.
   */
  private void updateLtv() {
    double customerExpenditurePerVisit = numSiteVisits > 0 ? 1.0 * totalAmountSpent / numSiteVisits : 0;
    double numberOfSiteVisitsPerWeek = weekYearKeys.size() > 0 ? 1.0 * numSiteVisits / weekYearKeys.size() : 0;
    ltv = 52 * (customerExpenditurePerVisit * numberOfSiteVisitsPerWeek) * 10;
  }

  /**
   * This updates the number of site visits and maintains a record of the observed (week, year) combinations.
   * @param weekYearKey
   */
  public void incNumSiteVisits(String weekYearKey) {
    this.numSiteVisits++;
    this.weekYearKeys.add(weekYearKey);
    updateLtv();
  }

  /**
   * This updates the total amount spent by the customer
   * @param amount
   */
  public void addToTotalAmountSpent(double amount) {
    this.totalAmountSpent += amount;
    updateLtv();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || ! (o instanceof Customer)) {
      return false;
    }
    Customer other = (Customer) o;
    return this.id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public int compareTo(Customer o) {
    if (this.ltv > o.ltv) {
      return 1;
    } else if (this.ltv < o.ltv) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return "[id=" + id + ", numSiteVisits=" + numSiteVisits + ", totalAmountSpent=" + totalAmountSpent
        + ", ltv=" + ltv  + "]";
  }
}
