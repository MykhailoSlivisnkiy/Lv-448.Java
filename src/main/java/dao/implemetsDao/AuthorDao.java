package dao.implemetsDao;

import dao.interfaceDao.AuthorDaoInterface;
import entities.Author;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AuthorDao implements AuthorDaoInterface {

    private final Connection connection;

    public AuthorDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Author author) {
        String query = "INSERT INTO authors"
                + "(id, first_name, last_name) "
                + "VALUE (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, author.getId());
            preparedStatement.setString(2, author.getAuthorFirstName());
            preparedStatement.setString(3, author.getAuthorLastName());
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteById(Long id) {

    }


    @Override
    public Optional<Author> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Author> findAll() {
        String query = "SELECT * FROM authors";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return extractAuthors(preparedStatement.getResultSet());
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Author> findBySurname() {
        return Optional.empty();
    }

    private List<Author> extractAuthors(ResultSet resultSet) throws SQLException {
        List<Author> authors = new ArrayList<>();
        while (resultSet.next()) {
            authors.add(Author.builder()
                    .id(resultSet.getLong("id"))
                    .authorFirstName(resultSet.getString("authorFirstName"))
                    .authorLastName(resultSet.getString("authorLastName"))
                    .build());
        }
        resultSet.close();
        return authors;
    }
}