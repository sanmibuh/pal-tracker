package io.pivotal.pal.tracker;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeEntryController {

  private final CounterService counter;
  private final GaugeService gauge;

  private final TimeEntryRepository timeEntryRepository;

  @Autowired
  public TimeEntryController(final CounterService counter, final GaugeService gauge, final TimeEntryRepository timeEntryRepository) {
    this.counter = counter;
    this.gauge = gauge;
    this.timeEntryRepository = timeEntryRepository;
  }

  @PostMapping("/time-entries")
  public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
    final TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);

    counter.increment("TimeEntry.created");
    gauge.submit("timeEntries.count", timeEntryRepository.list().size());

    return ResponseEntity.status(HttpStatus.CREATED).body(
      timeEntry) ;
  }

  @GetMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> read(@PathVariable final long id) {
    final TimeEntry timeEntry = timeEntryRepository.find(id);

    if (timeEntry == null) {
      return ResponseEntity.notFound().build();
    }

    counter.increment("TimeEntry.read");
    return ResponseEntity.ok(timeEntry);
  }

  @GetMapping("/time-entries")
  public ResponseEntity<List<TimeEntry>> list() {
    counter.increment("TimeEntry.listed");

    return ResponseEntity.ok(timeEntryRepository.list());
  }

  @PutMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> update(@PathVariable final long id, @RequestBody final TimeEntry expected) {
    final TimeEntry timeEntry = timeEntryRepository.update(id, expected);

    if (timeEntry == null) {
      return ResponseEntity.notFound().build();
    }

    counter.increment("TimeEntry.updated");
    return ResponseEntity.ok(timeEntry);
  }

  @DeleteMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> delete(@PathVariable final long id) {
    timeEntryRepository.delete(id);
    counter.increment("TimeEntry.deleted");
    gauge.submit("timeEntries.count", timeEntryRepository.list().size());

    return ResponseEntity.noContent().build();
  }
}
