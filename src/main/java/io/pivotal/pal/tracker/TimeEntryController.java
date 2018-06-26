package io.pivotal.pal.tracker;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

  private final TimeEntryRepository timeEntryRepository;

  @Autowired
  public TimeEntryController(final TimeEntryRepository timeEntryRepository) {
    this.timeEntryRepository = timeEntryRepository;
  }

  @PostMapping("/time-entries")
  public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
    return ResponseEntity.status(HttpStatus.CREATED).body(timeEntryRepository.create(timeEntryToCreate)) ;
  }

  @GetMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> read(@PathVariable final long id) {
    final TimeEntry timeEntry = timeEntryRepository.find(id);
    return toResponseEntity(timeEntry);
  }

  private ResponseEntity<TimeEntry> toResponseEntity(final TimeEntry timeEntry) {
    return timeEntry == null ?
      ResponseEntity.notFound().build() :
      ResponseEntity.ok(timeEntry);
  }

  @GetMapping("/time-entries")
  public ResponseEntity<List<TimeEntry>> list() {
    return ResponseEntity.ok(timeEntryRepository.list());
  }

  @PutMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> update(@PathVariable final long id, @RequestBody final TimeEntry expected) {
    final TimeEntry timeEntry = timeEntryRepository.update(id, expected);
    return toResponseEntity(timeEntry);
  }

  @DeleteMapping("/time-entries/{id}")
  public ResponseEntity<TimeEntry> delete(@PathVariable final long id) {
    timeEntryRepository.delete(id);
    return ResponseEntity.noContent().build();
  }
}
