package com.company;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class LTVUtil {
  // This implements a very straightforward parser for the expected input schema, which basically splits the string on ","
  // to determine the key-value pairs. Given that the input is actually JSON, we can use standard JSON parsing libraries
  // that can translate a JSON directly to an Object. For this implementation, we do not add any external dependencies.
  public static String[] getParsedLine(String input) {
    return input.replaceAll("\\[\\{", "").replaceAll("}]", ")").split(",");
  }

  // Utility function to get the week_year key identifying an event time. An example of this would be :
  // The year 2017, Week 10 would be represented by 2017_10
  public static String getWeekYearKey(String eventTime) throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Date date = dateFormat.parse(eventTime);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.WEEK_OF_YEAR);
  }

  // Utility function to get a BufferedReader given a file input path.
  public static BufferedReader bufferedReader(String path) throws Exception {
    return new BufferedReader(new FileReader(path));
  }

  // Utility function to get a Stream of lines given a file path.
  public static Stream<String> linesFromFilePath(String path) {
    try {
      return bufferedReader(path).lines();
    } catch (Exception e) {
    }
    return Stream.empty();
  }

  public static void writeToFile(List<String> input, String outputPath) throws Exception {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(outputPath)));
    input.stream()
            .forEach(line ->  {
                    try {
                      bufferedWriter.write(line + "\n");
                    } catch (Exception e) {
                    }
    });
    bufferedWriter.close();
  }
}
