package com.greatbit.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BookStorage {
     private  static Set<Book> books = new HashSet<>();
     static {
         books.add(new Book(UUID.randomUUID().toString(),"Гарри Поттер","Джоан Роулинг", "Фанатастика",400));
         books.add(new Book(UUID.randomUUID().toString(),"Война и мир","Лев Толстой","Роман", 1200));
         books.add(new Book(UUID.randomUUID().toString(),"Дюймовочка","Ханс Андерсен","Сказки", 100));
     }
     public static Set<Book> getBooks() {
         return books;
     }

}
