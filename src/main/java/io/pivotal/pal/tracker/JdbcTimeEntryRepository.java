package io.pivotal.pal.tracker;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

  private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
    rs.getLong("id"),
    rs.getLong("project_id"),
    rs.getLong("user_id"),
    rs.getDate("date").toLocalDate(),
    rs.getInt("hours")
  );

  private final ResultSetExtractor<TimeEntry> extractor =
    (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

  private final JdbcTemplate jdbcTemplate;

  public JdbcTimeEntryRepository(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public TimeEntry create(final TimeEntry timeEntry) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    final String sql = "INSERT INTO TIME_ENTRIES(PROJECT_ID, USER_ID, DATE, HOURS) VALUES(?, ?, ?, ?)";
    jdbcTemplate.update(
      connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, RETURN_GENERATED_KEYS);
        ps.setLong(1, timeEntry.getProjectId());
        ps.setLong(2, timeEntry.getUserId());
        ps.setDate(3, Date.valueOf(timeEntry.getDate()));
        ps.setInt(4, timeEntry.getHours());
        return ps;
      },
      keyHolder);
    return find(keyHolder.getKey().longValue());
  }

  @Override
  public TimeEntry find(long id) {
    return jdbcTemplate.query(
      "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
      new Object[]{id},
      extractor);
  }

  @Override
  public List<TimeEntry> list() {
    return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries", mapper);
  }

  @Override
  public TimeEntry update(long id, TimeEntry timeEntry) {
    final int count = jdbcTemplate.update("UPDATE time_entries " +
        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
        "WHERE id = ?",
      timeEntry.getProjectId(),
      timeEntry.getUserId(),
      Date.valueOf(timeEntry.getDate()),
      timeEntry.getHours(),
      id);

    if (count == 0) {
      return null;
    }

    return find(id);
  }

  @Override
  public void delete(long id) {
    jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id);
  }
}
