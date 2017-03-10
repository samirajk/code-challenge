package com.company;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is the main driver class which houses the Ingest and TopXSimpleLTVCustomers functionality.
 */
public class Main {
  /**
   * This function implements the top level logic for determining the event type and calling the
   * appropriate "Event" implementation to convert it to an object.
   * @param event The input string which represents and event
   * @return An output object that implements "Event"
   */
  public static Event decode(String event) {
    try {
      if (event.contains("\"type\": \"" + Event.EventType.CUSTOMER.toString())) {
        return new CustomerEvent(event);
      } else if (event.contains("\"type\": \"" + Event.EventType.SITE_VISIT.toString())) {
        return new SiteVisitEvent(event);
      } else if (event.contains("\"type\": \"" + Event.EventType.IMAGE.toString())) {
        return new ImageEvent(event);
      } else if (event.contains("\"type\": \"" + Event.EventType.ORDER.toString())) {
        return new OrderEvent(event);
      }
    } catch (Exception e) {
      System.err.println("Failed to parse event : " + event);
      e.printStackTrace();
    }

    // We simply log an error and return null for all "non-parseable" events.
    return null;
  }

  /**
   * This implements the ingest functionality, given an input path and an output map of
   * CustomerId -> Customer. Even newly observed customer is added as an entry in the output map.
   * @param inputPath The input path to the events data file
   * @param output The output Map from CustomerId -> Customer.
   */
  public static void ingest(String inputPath, Map<String, Customer> output) {
    // Read each line from the input path and decode it into an Event object (ignoring null events).
    // For every event, if
    //    -> The event is of type CUSTOMER, check whether a known customer with the same ID exists in the output map
    //       If yes, then this can be skipped. Or else create a new entry in the map
    //    -> For other event types, pass the event based on its ID to the appropriate Customer entity,
    //       which knows how to consume this event.
    //       If a non-customer event is found for a customer that is unknown, we simply log a warning message
    //       skipping the same.
    LTVUtil.linesFromFilePath(inputPath)
        .filter(event -> !event.trim().isEmpty())
        .map(event -> decode(event))
        .filter(event -> event != null)
        .forEach(event -> {
          if (event.getEventType() == Event.EventType.CUSTOMER) {
            if (!output.containsKey(event.getId())) {
              Customer customer = new Customer(event.getId());
              output.put(event.getId(), customer);
            }
          } else {
            if (output.containsKey(event.getId())) {
              output.get(event.getId()).consumeEvent(event);
            } else {
              // This is for out of sequence events. e.g. A Site visit or order event received before the customer event
              System.out.println("Warning : Event " + event.getEventType()
                  + " observed for unknown customer (" + event.getId() + "). " +
                  "Will create a new customer entity and consume the event");
              Customer customer = new Customer(event.getId());
              customer.consumeEvent(event);
              output.put(event.getId(), customer);
            }
          }
        });
  }

  /**
   * This implements the functionality to compute the list of top X customers with the highest SimpleLTV value
   * @param input
   * @param x
   * @return
   */
  public static List<Customer> TopXSimpleLTVCustomers(Customer[] input, int x) {
    PartialSort<Customer> partialSort = new PartialSort<>(input);
    List<Customer> output = Arrays.asList(partialSort.partialSortTopK(x));
    return x > output.size() ? output : output.subList(0, x);
  }

  public static void main(String[] args) throws Exception {
    String inputPath =  System.getProperty("user.dir")
        + File.separator + "input"
        + File.separator + "input.txt";
    int x = Integer.parseInt(args[0]);
    Map<String, Customer> customers = new HashMap<>();

    // Ingest all customer data
    ingest(inputPath, customers);

    // Perform top K computation based on partial sorting (linear time) and return the topK customers
    List<Customer> topKCustomers = TopXSimpleLTVCustomers(customers.values().toArray(new Customer[0]), x);

    String outputPath = System.getProperty("user.dir")
       + File.separator + "output"
       + File.separator + "output.txt";
    LTVUtil.writeToFile(topKCustomers.stream()
    .map(customer -> customer.toString()).collect(Collectors.toList()), outputPath);
  }
}
   