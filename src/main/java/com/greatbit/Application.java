package com.greatbit;

import com.greatbit.model.Book;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class Application {
    @Value("${jdbcUrl}") String jdbcUrl;
    @Value("${jdbcUser}") String jdbcUser;
    @Value("${jdbcPassword}") String jdbcPassword;
    @Bean
    public DataSource h2DataSource(){
       JdbcDataSource dataSource =new JdbcDataSource();
       dataSource.setURL(jdbcUrl);
       dataSource.setUser(jdbcUser);
       dataSource.setPassword(jdbcPassword);
       return dataSource;
    }
    @Bean
    public CommandLineRunner cmd (DataSource dataSource){
        return args -> {
            try(InputStream inputStream = this.getClass().getResourceAsStream("/initial.sql")) {
                String sql =  new String(inputStream.readAllBytes());
                try (
                    Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement()
                ) {
                    statement.executeUpdate(sql);
                    String insertSql = "INSERT INTO book (name, author, genre, pages) VALUES (?,?,?,?)";
                    try(PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        preparedStatement.setString(1,"java book");
                        preparedStatement.setString(2,"ero");
                        preparedStatement.setString(3,"roman");
                        preparedStatement.setInt(4,123);
                        preparedStatement.executeUpdate();
                    }

                    System.out.println("Printing books from database ");
                   ResultSet rs = statement.executeQuery("SELECT  book_id, name, author, genre, pages FROM book");
                    while (rs.next()) {
                     Book book =new Book(rs.getString(1),rs.getString(2),
                             rs.getString(3), rs.getString(4),
                             rs.getInt(5));
                        System.out.println(book);
                    }


                }

            }
        };


    }

    public static void main(String[] args) {


        SpringApplication.run(Application.class, args);
    }
}