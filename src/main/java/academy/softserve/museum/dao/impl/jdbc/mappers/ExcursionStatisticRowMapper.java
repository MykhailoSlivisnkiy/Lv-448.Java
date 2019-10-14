package academy.softserve.museum.dao.impl.jdbc.mappers;

import academy.softserve.museum.entities.Excursion;
import academy.softserve.museum.entities.statistic.ExcursionStatistic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ExcursionStatisticRowMapper implements RowMapper<ExcursionStatistic> {

    @Override
    public ExcursionStatistic mapRow(ResultSet resultSet) {
        ExcursionStatistic statistic = new ExcursionStatistic();
        Map<Excursion, Integer> excursionCountMap = new HashMap<>();
        ExcursionRowMapper rowMapper = new ExcursionRowMapper();

        try {
            while (resultSet.next()) {
                excursionCountMap.put(rowMapper.mapRow(resultSet), resultSet.getInt("excursion_count"));
            }

            statistic.setExcursionCountMap(excursionCountMap);

            return statistic;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
