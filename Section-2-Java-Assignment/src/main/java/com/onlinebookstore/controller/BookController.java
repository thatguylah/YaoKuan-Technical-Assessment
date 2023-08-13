package com.onlinebookstore.controller;

import com.onlinebookstore.model.entity.Book;
import com.onlinebookstore.model.dto.SearchBookDTO;
import com.onlinebookstore.model.dto.FilterBookDTO;
import com.onlinebookstore.service.BookService;
import javax.validation.Valid;

import com.onlinebookstore.service.SearchService;
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
    public BookController(BookService bookService, SearchService searchService) {
        this.bookService = bookService;
        this.searchService = searchService;
    }
    public final SearchService searchService;

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        logger.info("Received book data: {}", book);
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
        logger.info("Removing book with id: {}", id);
        bookService.removeBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<Book> updateBookQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        logger.info("Updating quantity for book id: {} with quantity: {}", id, quantity);
        Book updatedBook = bookService.updateBookQuantity(id, quantity);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/{id}/quantity")
    public ResponseEntity<Integer> getBookQuantity(@PathVariable Long id) {
        logger.info("Retrieving quantity for book id: {}", id);
        Integer quantity = bookService.getBookQuantity(id);
        return ResponseEntity.ok(quantity);
    }

    /**The below method performs a search and is capable of taking in a payload of type
    { author: Optional<String> , title: Optional <String>, isbn : Optional<String> }
    where any of them can be empty, but not all of them. The search method uses fuzzy matching for strings.
     */
    @PostMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestBody SearchBookDTO searchCriteria) {
        logger.info("Searching for books using the following criteria:{}",searchCriteria);
        List<Book> searchedBooks = searchService.combinedSearch(searchCriteria);
        return ResponseEntity.ok(searchedBooks);

    }

    /**The below method performs a filter and is capable of taking in a payload of type
     { minPrice: Optional<int> , maxPrice: Optional<int>, avail :Optional<Boolean> }
     where any of them can be empty, but not all of them. The filter method finds prices in the range between
     minPrice and maxPrice or above minPrice or below maxPrice if either is empty. If avail is True,
     filters results where quantity > 0.
     */
    @PostMapping("/filter")
    public ResponseEntity<List<Book>> filterBooks(@RequestBody FilterBookDTO filterCriteria) {
        logger.info("Filtering for books using the following criteria:{}",filterCriteria);
        List<Book> searchedBooks = searchService.combinedFilter(filterCriteria);
        return ResponseEntity.ok(searchedBooks);

    }
}
