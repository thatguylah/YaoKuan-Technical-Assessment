package com.onlinebookstore.repository;

import com.onlinebookstore.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);
    List<Book> findByAuthorContaining(String author);
    List<Book> findByIsbnContaining(String isbn);
    List<Book> findByPriceBetween(double minPrice, double maxPrice);
    List<Book> findByQuantityInStockGreaterThan(Integer quantityInStock);
    List<Book> findByPriceBetweenAndQuantityInStockGreaterThan(double minPrice, double maxPrice, Integer quantityInStock);

    @Query("SELECT b FROM Book b WHERE "
            + "(?1 IS NULL OR b.title LIKE %?1%) AND "
            + "(?2 IS NULL OR b.author LIKE %?2%) AND "
            + "(?3 IS NULL OR b.isbn LIKE %?3%)")
    List<Book> findByTitleContainingAndAuthorContainingAndIsbnContaining(String title, String author, String isbn);

}

