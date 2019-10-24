package inc.softserve.dao.implementations;

import inc.softserve.dao.interfaces.CityDao;
import inc.softserve.dao.interfaces.HotelDao;
import inc.softserve.entities.Hotel;
import inc.softserve.entities.stats.HotelStats;
import inc.softserve.entities.stats.time_utils.Day;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// @Slf4j
public class HotelDaoJdbc implements HotelDao {

    private static final String TABLE_NAME = "hotels";

    private final Connection connection;
    private final CityDao cityDao;

    public HotelDaoJdbc(Connection connection, CityDao cityDao) {
        this.connection = connection;
        this.cityDao = cityDao;
    }

    @Override
    public Set<Hotel> findAll() {
        String query = "SELECT * FROM hotels";
        try (PreparedStatement prepStat = connection.prepareStatement(query)) {
            ResultSet resultSet = prepStat.executeQuery();
            return extractHotels(resultSet).collect(Collectors.toSet());
        } catch (SQLException e) {
         //   log.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public Optional<Hotel> findById(Long hotelId) {
        String query = "SELECT * FROM hotels WHERE id = ?";
        try (PreparedStatement prepStat = connection.prepareStatement(query)) {
            prepStat.setLong(1, hotelId);
            ResultSet resultSet = prepStat.executeQuery();
            return extractHotels(resultSet).findAny();
        } catch (SQLException e) {
         //   log.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public Set<Hotel> findHotelsByCityId(Long cityId) {
        String query = "SELECT * FROM hotels WHERE city_id = ?";
        try (PreparedStatement prepStat = connection.prepareStatement(query)) {
            prepStat.setLong(1, cityId);
            ResultSet resultSet = prepStat.executeQuery();
            return extractHotels(resultSet).collect(Collectors.toSet());
        } catch (SQLException e) {
          //  log.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public Set<Hotel> findHotelsByCityIdAndPeriod(Long cityId, LocalDate startPeriod, LocalDate endPeriod){
        String query = "SELECT DISTINCT hotels.* FROM rooms " +
                "INNER JOIN cities " +
                "ON rooms.city_id = cities.id " +
                "LEFT JOIN bookings " +
                "ON rooms.id = bookings.room_id " +
                "INNER JOIN hotels " +
                "ON rooms.hotel_id = hotels.id " +
                "WHERE rooms.city_id = ? " +
                "AND order_date IS NULL " +
                "OR (checkin >= ? AND checkout <= ?)";
        try (PreparedStatement prepStat = connection.prepareStatement(query)) {
            prepStat.setLong(1, cityId);
            prepStat.setDate(2, Date.valueOf(startPeriod));
            prepStat.setDate(3, Date.valueOf(endPeriod));
            ResultSet resultSet = prepStat.executeQuery();
            return extractHotels(resultSet).collect(Collectors.toSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<HotelStats> calcStats() {
        String query = "SELECT hotel_name, " +
                "COUNT(usr_id) AS count_users, " +
                "AVG(DATEDIFF(checkout, checkin)) AS average_booking_time " +
                "FROM hotels " +
                "INNER JOIN bookings " +
                "ON hotels.id = bookings.hotel_id " +
                "GROUP BY hotel_name";
        try (PreparedStatement prepStat = connection.prepareStatement(query)) {
            ResultSet resultSet = prepStat.executeQuery();
            return extractStatistics(resultSet).collect(Collectors.toList());
        } catch (SQLException e) {
         //   log.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private Stream<HotelStats> extractStatistics(ResultSet rs) throws SQLException {
        Stream.Builder<HotelStats> builder = Stream.builder();
        while (rs.next()){
            HotelStats hotelStats = HotelStats.builder()
                    .hotelName(rs.getString("hotel_name"))
                    .clients(rs.getInt("count_users"))
                    .averageBookingTime(new Day(rs.getInt("average_booking_time")))
                    .build();
            builder.accept(hotelStats);
        }
        return builder.build();
    }

    private Stream<Hotel> extractHotels(ResultSet rs) throws SQLException {
        Stream.Builder<Hotel> builder = Stream.builder();
        while (rs.next()){
            Hotel hotel = new Hotel();
            hotel.setId(rs.getLong("id"));
            hotel.setHotelName(rs.getString("hotel_name"));
            hotel.setStreet(rs.getString("street"));
            hotel.setStreetNumber(rs.getString("street_number"));
            hotel.setStars(Hotel.Stars.valueOf(rs.getString("stars")));
            hotel.setCity(cityDao
                    .findById(rs.getLong("city_id"))
                    .orElseThrow());
            builder.accept(hotel);
        }
        return builder.build();
    }
}
