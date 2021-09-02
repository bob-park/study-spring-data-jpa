package study.datajpa.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

public class CustomId {

  private String id;

  public static String generate() {

    LocalDateTime now = LocalDateTime.now();

    long time = now.toInstant(ZoneOffset.UTC).toEpochMilli();

    String s = Base64.getEncoder().encodeToString(String.valueOf(time).getBytes());

    return s.replaceAll("[\\W]", "");
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
