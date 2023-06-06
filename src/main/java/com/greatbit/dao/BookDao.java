package com.greatbit.dao;

import com.greatbit.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> findall();
    Book save(Book book);
    Book getById(String bookId);
    Book update(Book book);
    void delete(Book book);
    void  deleteAll();

}
