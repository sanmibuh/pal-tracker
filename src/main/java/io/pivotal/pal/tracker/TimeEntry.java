package io.pivotal.pal.tracker;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeEntry {

  private long id;
  private long projectId;
  private long userId;
  private LocalDate date;
  private int hours;

  public TimeEntry(final long projectId, final long userId, final LocalDate date, final int hours) {
    this(0L, projectId, userId,date, hours);
  }

  public TimeEntry(final long id, final long projectId, long userId, final LocalDate date, final int hours) {
    this.id = id;
    this.projectId = projectId;
    this.userId = userId;
    this.date = date;
    this.hours = hours;
  }

  public void setFrom(final TimeEntry timeEntry) {
    this.projectId = timeEntry.projectId;
    this.userId = timeEntry.userId;
    this.date = timeEntry.date;
    this.hours = timeEntry.hours;
  }
}
