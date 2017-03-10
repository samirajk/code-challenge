package com.company;

public class SiteVisitEvent implements Event {
  public final EventType eventType;
  public final String customerId;
  public final String weekYearKey;

  @Override
  public EventType getEventType() {
    return EventType.SITE_VISIT;
  }

  @Override
  public String getId() {
    return customerId;
  }

  @Override
  public String getWeekYearKey() {
    return weekYearKey;
  }

  public SiteVisitEvent(String input) throws Exception {
    this.eventType = EventType.SITE_VISIT;
    String parts[] = LTVUtil.getParsedLine(input);
    String eventTime = "";
    String customerId = "";
    for (String key : parts) {
      if (key.contains("event_time")) {
        eventTime = key.split("\":")[1].replaceAll("\"", "").trim();
      } else if (key.contains("customer_id")) {
        customerId = key.split("\":")[1].replaceAll("\"", "").trim();
      }
    }
    this.customerId = customerId;
    this.weekYearKey = LTVUtil.getWeekYearKey(eventTime);
  }
}
