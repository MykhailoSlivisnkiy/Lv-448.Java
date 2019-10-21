package academy.softserve.museum.dao.impl.jdbc;

import academy.softserve.museum.dao.TimetableDao;
import academy.softserve.museum.dao.impl.jdbc.mappers.EmployeeRowMaper;
import academy.softserve.museum.dao.impl.jdbc.mappers.ExcursionRowMapper;
import academy.softserve.museum.dao.impl.jdbc.mappers.IdRowMapper;
import academy.softserve.museum.dao.impl.jdbc.mappers.TimetableRowMapper;
import academy.softserve.museum.entities.Employee;
import academy.softserve.museum.entities.Excursion;
import academy.softserve.museum.entities.Timetable;
import academy.softserve.museum.util.JdbcUtils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class JdbcTimetableDao implements TimetableDao {

    private static JdbcTimetableDao instance;
    private final Connection connection;

    private JdbcTimetableDao(Connection connection) {
        this.connection = connection;
    }

    public static JdbcTimetableDao getInstance(Connection connection) {
        if (instance == null) {
            instance = new JdbcTimetableDao(connection);
        }

        return instance;
    }

    @Override
    public long save(Timetable objectToSave) {
        String SAVE_TIMETABLE = "INSERT INTO timetable(employee_id, excursion_id, date_start, date_end) " +
                "VALUES (?, ?, ?, ?)";

        JdbcUtils.update(connection, SAVE_TIMETABLE, objectToSave.getEmployee().getId(), objectToSave.getExcursion().getId(),
                objectToSave.getDateStart(), objectToSave.getDateEnd());

        return getLastSavedObjectId();
    }

    @Override
    public void deleteById(long id) {
        String DELETE_TIMETABLE_BY_ID = "DELETE FROM timetable WHERE id = ?";

        JdbcUtils.update(connection, DELETE_TIMETABLE_BY_ID, id);
    }

    @Override
    public Optional<Timetable> findById(long id) {
        String FIND_TIMETABLE_BY_ID = "SELECT tt.id AS timetable_id, tt.date_start, tt.date_end, e.id AS employee_id, " +
                "e.first_name, e.last_name, e.position, e.password, e.login, " +
                "ex.id AS excursion_id, ex.name AS excursion_name " +
                "FROM timetable AS tt " +
                "INNER JOIN employees AS e ON tt.employee_id = e.id " +
                "INNER JOIN excursion AS ex ON tt.excursion_id = ex.id " +
                "WHERE tt.id = ?";

        return JdbcUtils.queryForObject(connection, FIND_TIMETABLE_BY_ID, new TimetableRowMapper(), id);
    }

    @Override
    public List<Timetable> findAll() {
        String FIND_ALL_TIMETABLES = "SELECT tt.id AS timetable_id, tt.date_start, tt.date_end, e.id AS employee_id, " +
                "e.first_name, e.last_name, e.position, e.password, e.login, " +
                "ex.id AS excursion_id, ex.name AS excursion_name " +
                "FROM timetable AS tt " +
                "INNER JOIN employees AS e ON tt.employee_id = e.id " +
                "INNER JOIN excursion AS ex ON tt.excursion_id = ex.id ";
        ;

        return JdbcUtils.query(connection, FIND_ALL_TIMETABLES, new TimetableRowMapper());
    }

    @Override
    public int update(Timetable newObject) {
        String UPDATE_TIMETABLE = "UPDATE timetable set employee_id = ?, excursion_id = ?, date_start = ?," +
                "date_end = ? WHERE id = ?";

        return JdbcUtils.update(connection, UPDATE_TIMETABLE, newObject.getEmployee().getId(), newObject.getExcursion().getId(),
                newObject.getDateStart(), newObject.getDateEnd(), newObject.getId());
    }

    private long getLastSavedObjectId() {
        String QUERY = "SELECT MAX(id) AS last_id FROM timetable";

        return JdbcUtils.queryForObject(connection, QUERY, new IdRowMapper()).
                orElseThrow(() -> new RuntimeException("Can't get last saved object id"));
    }
}
