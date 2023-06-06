package com.greatbit.dao;

import com.greatbit.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class BookDaoImpl implements BookDao {
    private final DataSource dataSource;
@Autowired
    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Book> findall() {
    final String selectSQL = "SELECT  book_id,name,author,genre,pages FROM book";
    List <Book> books = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(selectSQL)
    ) {
        while(rs.next()) {
            Book book = new Book(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getInt(5));
            books.add(book);
        }


    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
        return books;
    }

    @Override
    public Book save(Book book) {
        String insertSql = "INSERT INTO book (name, author, genre, pages) VALUES (?,?,?,?)";
        try(
               Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getGenre());
            preparedStatement.setInt(4,book.getPages());
            preparedStatement.executeUpdate();
            try (ResultSet rs= preparedStatement.getGeneratedKeys())
            {
                rs.next();
                book.setId(rs.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public Book getById(String bookId) {
        String getByIdSql = "SELECT book_id,name,author,genre,pages FROM book WHERE book_id = ?";
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getByIdSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1,Integer.parseInt(bookId));

            try(ResultSet rs = preparedStatement.executeQuery()) {
                if(!rs.next()) {
                    throw new RuntimeException(String.format("Книга %s не была найдена",bookId));
                }
                return new Book(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getInt(5));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book update(Book book) {
    if (Objects.isNull(book.getId())) {
        throw new RuntimeException("Не возможно обновить книгу");
    }
        String updateSql = "UPDATE book SET name=?, author=?, genre =?, pages =? WHERE book_id = ?" ;
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getGenre());
            preparedStatement.setInt(4,book.getPages());
            preparedStatement.setString(5, book.getId());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public void delete(Book book) {
        String deleteByIdSql = "DELETE FROM book WHERE book_Id = ?";
        try(Connection connection =dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(deleteByIdSql)
        ) {
            ps.setString(1, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void deleteAll() {
    String deleteSql = "TRUNCATE TABLE book";
    try(Connection connection =dataSource.getConnection();
    Statement statement = connection.createStatement();
    ) {
        statement.executeUpdate(deleteSql);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    }
}
