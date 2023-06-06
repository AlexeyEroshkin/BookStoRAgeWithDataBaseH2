package com.greatbit.dao;

import com.greatbit.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {"jdbcUrl = jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"})

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookDaoImplTest {
    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void beforeEach(){
        bookDao.deleteAll();
    }

    @Test
    public  void contextCreated(){

    }
    @Test
    public void saveSavesDataToDbAndReturnEntityWithId(){
        Book book = bookDao.save(new Book("book name", "book author", "book genre", 2));
        assertThat(book.getId()).isNotBlank();
        assertThat(bookDao.findall()).extracting("id").containsExactly(book.getId());
    }


    @Test
    void deleteAllDeletesAllData(){
        bookDao.save(new Book("book name","book author","book genre",2));
        assertThat(bookDao.findall()).isNotEmpty();
        bookDao.deleteAll();
        assertThat(bookDao.findall()).isEmpty();
    }
    @Test
    void findAllReturnsAllBook(){
        assertThat(bookDao.findall()).isEmpty();
        bookDao.save(new Book("book name", "book author", "book genre", 2));
        assertThat(bookDao.findall()).isNotEmpty();
    }
    @Test
    void getByIdThrowsRuntimeExceptionIsNotBookFound(){
        assertThatThrownBy(()->bookDao.getById("1")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getByIdReturnsCorrectBook(){
        Book book = bookDao.save(new Book("book name", "book author", "book genre", 2));
        bookDao.save(new Book("other book name 1", "other book author 2", " other book genre 3", 50));
        assertThat(bookDao.getById(book.getId())).isNotNull().extracting("name").isEqualTo(book.getName());

    }
    @Test
    void UpdateUpdatesDataInDB(){
        Book book = bookDao.save(new Book("book name", "book author", "book genre", 2));
        book.setName("new name");
        bookDao.update(book);
       assertThat( bookDao.getById(book.getId()).getName()).isEqualTo("new name");

    }
    @Test
    void updateThrowsExceptionOnUpdatingSavedBook(){
        assertThatThrownBy(()->bookDao.update(new Book("book name", "book author", "book genre", 2))).
                isInstanceOf(RuntimeException.class);
    }
    @Test
    void deleteDeleteCorrectData(){
        Book bookToKeep = bookDao.save(new Book("book name", "book author", "book genre", 2));
        Book bookToDelete = bookDao.save(new Book("other book name 1", "other book author 2", " other book genre 3", 50));
        bookDao.delete(bookToDelete);
        assertThat(bookDao.getById(bookToKeep.getId())).isNotNull();
        assertThatThrownBy(()->bookDao.getById(bookToDelete.getId())).isInstanceOf(RuntimeException.class);

    }





}