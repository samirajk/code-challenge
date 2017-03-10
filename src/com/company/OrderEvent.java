package com.company;

public class OrderEvent implements Event {
  public final EventType eventType;
  public final String customerId;
  public final float totalAmount;
  public final String weekYearKey;

  @Override
  public EventType getEventType() {
    return EventType.ORDER;
  }

  @Override
  public String getId() {
    return customerId;
  }

  public OrderEvent(String input) throws Exception {
    this.eventType = EventType.ORDER;
    String customerId = "";
    float totalAmount = 0;
    String eventTime = "";
    String parts[] = LTVUtil.getParsedLine(input);
    for (String key : parts) {
      if (key.contains("customer_id")) {
        customerId = key.split("\":")[1].replaceAll("\"", "").trim();
      } else if (key.contains("total_amount")) {
        totalAmount = Float.parseFloat(key.split("\":")[1].trim().replaceAll("\"", "").split(" ")[0].trim());
      } else if (key.contains("event_time")) {
        eventTime = key.split("\":")[1].replaceAll("\"", "").trim();
      }
    }
    this.customerId = customerId;
    this.totalAmount = totalAmount;
    this.weekYearKey = LTVUtil.getWeekYearKey(eventTime);
  }

  @Override
  public String getWeekYearKey() {
    return weekYearKey;
  }
}
