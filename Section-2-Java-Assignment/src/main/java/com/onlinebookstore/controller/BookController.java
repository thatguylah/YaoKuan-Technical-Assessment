package com.onlinebookstore.controller;

import com.onlinebookstore.model.entity.Book;
import com.onlinebookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        logger.debug("Received book data: {}", book);
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> bookOpt = bookService.getBook(id);
        return bookOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBook(@PathVariable Long id) {
        logger.debug("Removing book with id: {}", id);
        bookService.removeBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<Book> updateBookQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        logger.debug("Updating quantity for book id: {} with quantity: {}", id, quantity);
        Book updatedBook = bookService.updateBookQuantity(id, quantity);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/{id}/quantity")
    public ResponseEntity<Integer> getBookQuantity(@PathVariable Long id) {
        logger.debug("Retrieving quantity for book id: {}", id);
        Integer quantity = bookService.getBookQuantity(id);
        return ResponseEntity.ok(quantity);
    }

}
