package com.onlinebookstore.repository;

import com.onlinebookstore.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);
    List<Book> findByAuthorContaining(String author);
    List<Book> findByIsbnContaining(String isbn);
    List<Book> findByPriceBetween(double minPrice, double maxPrice);
    List<Book> findByQuantityInStockGreaterThan(Integer quantityInStock);
    List<Book> findByPriceBetweenAndQuantityInStockGreaterThan(double minPrice, double maxPrice, Integer quantityInStock);

}

