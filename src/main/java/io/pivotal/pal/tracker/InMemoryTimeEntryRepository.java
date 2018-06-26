package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

  final List<TimeEntry> data = new ArrayList<>();

  public TimeEntry create(TimeEntry timeEntry) {
    timeEntry.setId(data.size() + 1);
    data.add(timeEntry);
    return timeEntry;
  }

  public TimeEntry find(final long id) {
    return filter(id).orElse(null);
  }

  private Optional<TimeEntry> filter(final long id) {
    return data.stream().filter(it -> it.getId() == id).findFirst();
  }

  public TimeEntry update(final long id, final TimeEntry timeEntry) {
    final Optional<TimeEntry>  existingTimeEntry = filter(id);

    existingTimeEntry.ifPresent(it -> {
      it.setFrom(timeEntry);
    });

    return existingTimeEntry.orElse(null);
  }

  public void delete(final long id) {
    filter(id).ifPresent(it -> data.remove(it));
  }

  public List<TimeEntry> list() {
    return data;
  }
}
