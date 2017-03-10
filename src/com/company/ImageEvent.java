package com.company;

public class ImageEvent implements Event {
  public final EventType eventType;
  public final String id;
  public final String weekYearKey;

  @Override
  public EventType getEventType() {
    return EventType.IMAGE;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getWeekYearKey() {
    return weekYearKey;
  }

  public ImageEvent(String input) throws Exception {
    this.eventType = EventType.IMAGE;
    String customerId = "";
    String eventTime = "";
    String parts[] = LTVUtil.getParsedLine(input);
    for (String key : parts) {
      if (key.contains("customer_id")) {
        customerId = key.split("\":")[1].replaceAll("\"", "").trim();
      } else if (key.contains("event_time")) {
        eventTime = key.split("\":")[1].replaceAll("\"", "").trim();
      }
    }
    this.id = customerId;
    this.weekYearKey = LTVUtil.getWeekYearKey(eventTime);
  }
}
