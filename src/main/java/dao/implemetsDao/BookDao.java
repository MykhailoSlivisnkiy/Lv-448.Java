package dao.implemetsDao;

import dao.interfaceDao.BookDaoInterface;
import entities.Book;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class BookDao implements BookDaoInterface {

    private final Connection connection;
    private Book Book;

    public BookDao(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void save(Book book) {
        String query = "INSERT INTO books "
                + "(id, amount_of_instances, title, release_date, category)"
                + "VALUE (?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, book.getId());
            preparedStatement.setLong(2, book.getAmountOfInstances());
            preparedStatement.setString(3, book.getTitle());
            preparedStatement.setDate(4, Date.valueOf(book.getReleaseDate()));
            preparedStatement.setString(5, book.getCategory());
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public List<Book> findAll() {
        String query = "SELECT * FROM books";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return extractBooks(preparedStatement.executeQuery()).collect(Collectors.toList());
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public List<Book> findAllBooksBySubAuthor(Long authorId) {
        String query = "select id_book from book_sub_authors where id_author = ?;";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(
                        findById(resultSet.getLong("id_book")).orElse(null)
                );
            }
            return books;
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> findAllByAuthorName(Long authorId) {
        String query = "SELECT * FROM books WHERE id_author = ?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, authorId);
            books = extractBooks(preparedStatement.executeQuery()).collect(Collectors.toList());
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
        }
        return books;
    }


    private Stream<Book> extractBooks(ResultSet resultSet) throws SQLException {
        Stream.Builder<Book> bookBuilder = Stream.builder();
        while (resultSet.next()) {
            bookBuilder.add(
                    Book.builder()
                            .id(resultSet.getLong("id"))
                            .amountOfInstances(resultSet.getInt("amount_of_instances"))
                            .title(resultSet.getString("title"))
                            .releaseDate(resultSet.getDate("release_date").toLocalDate())
                            .category(resultSet.getString("category"))
                            .build());

        }
        resultSet.close();
        return bookBuilder.build();
    }

    @Override
    public void update(Long id, Book book) {
        //
    }

    @Override
    public void deleteById(Long id) {
        String query = "SELECT * FROM books";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String query = "SELECT * FROM book WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return extractBooks(preparedStatement.executeQuery()).findAny();
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException();
        }
    }

}
