package com.company;

public class CustomerEvent implements Event {
  public final EventType eventType;
  public final String id;

  @Override
  public EventType getEventType() {
    return EventType.CUSTOMER;
  }

  @Override
  public String getWeekYearKey() {
    return "";
  }

  @Override
  public String getId() {
    return id;
  }

  public CustomerEvent(String input) {
    String parts[] = LTVUtil.getParsedLine(input);
    this.eventType = EventType.CUSTOMER;
    String id = "";
    for (String key : parts) {
      if (key.contains("key")) {
        id = key.split("\":")[1].replaceAll("\"", "").trim();
      }
    }
    this.id = id;
  }
}
