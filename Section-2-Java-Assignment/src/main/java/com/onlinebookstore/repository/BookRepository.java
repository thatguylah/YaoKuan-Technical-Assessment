package com.onlinebookstore.repository;

import com.onlinebookstore.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // This interface will inherit the standard CRUD operations
}

