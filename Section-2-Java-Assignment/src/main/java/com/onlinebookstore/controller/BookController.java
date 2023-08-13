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

/**
 * A RESTful controller that manages and provides endpoints related to books.
 */
@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;
    public final SearchService searchService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    // Helper method to check for null or empty string
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Helper method to check for null numbers (Integer, Double, etc.)
    private boolean isNullOrEmpty(Number value) {
        return value == null;
    }

    @Autowired
    public BookController(BookService bookService, SearchService searchService) {
        this.bookService = bookService;
        this.searchService = searchService;
    }

    /**
     * Adds a new book to the store.
     * Any "id" value in the payload is ignored as it's supposed to be system-generated.
     * Checks if any required fields are empty or null and rejects payload if true.
     *
     * @param book The book entity to be added.
     * @return ResponseEntity containing the added book or an error message.
     */
    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {

        logger.info("Received book data: {}", book);

        // Ensure that the book ID is set to null
        book.setId(null);

        // Check if any essential field is null or empty
        if (isNullOrEmpty(book.getTitle()) ||
                isNullOrEmpty(book.getAuthor()) ||
                isNullOrEmpty(book.getIsbn()) ||
                isNullOrEmpty(book.getPrice())||
                        isNullOrEmpty(book.getQuantity()))
    {
            return ResponseEntity.badRequest().body(book);
        }

        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    /**
     * Retrieves a specific book by its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return ResponseEntity containing the retrieved book or an error message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> bookOpt = bookService.getBook(id);
        return bookOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Fetches all books available in the store.
     *
     * @return List of all books.
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Removes a specific book by its ID.
     *
     * @param id The ID of the book to be removed.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBook(@PathVariable Long id) {
        logger.info("Removing book with id: {}", id);
        bookService.removeBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the quantity of a specific book.
     *
     * @param id       The ID of the book to be updated.
     * @param quantity The new quantity for the book.
     * @return ResponseEntity containing the updated book or an error message.
     */
    @PutMapping("/{id}/quantity")
    public ResponseEntity<Book> updateBookQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        logger.info("Updating quantity for book id: {} with quantity: {}", id, quantity);
        Book updatedBook = bookService.updateBookQuantity(id, quantity);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Fetches the quantity of a specific book.
     *
     * @param id The ID of the book whose quantity is to be fetched.
     * @return ResponseEntity containing the quantity of the book.
     */
    @GetMapping("/{id}/quantity")
    public ResponseEntity<Integer> getBookQuantity(@PathVariable Long id) {
        logger.info("Retrieving quantity for book id: {}", id);
        Integer quantity = bookService.getBookQuantity(id);
        return ResponseEntity.ok(quantity);
    }

    /**
     * Searches for books based on the given search criteria. The criteria can include fields such as
     * title, author, and ISBN. The search operation uses fuzzy matching to provide results that closely match
     * the provided criteria, even if they're not exact. For instance, if "Har" is provided in the title,
     * it may return books titled "Harry Potter" or "Harbour".
     *
     * @param searchCriteria The DTO containing fields such as title, author, and ISBN to specify the search parameters.
     *                       Each of these fields is optional, allowing for flexible searches.
     * @return ResponseEntity containing the list of books that match the provided criteria. If no matches are found,
     *         an empty list is returned.
     */
    @PostMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestBody SearchBookDTO searchCriteria) {
        logger.info("Searching for books using the following criteria:{}",searchCriteria);
        List<Book> searchedBooks = searchService.combinedSearch(searchCriteria);
        return ResponseEntity.ok(searchedBooks);

    }

    /**
     * Filters books based on the provided criteria. This method is designed to narrow down results
     * by focusing on specific attributes such as price range and availability. The filter can be set
     * to focus on a range of prices or filter out books that aren't available in stock.
     *
     * For example, specifying a minPrice and maxPrice will return books priced within that range.
     * Specifying avail as true will return only those books that are currently in stock.
     *
     * @param filterCriteria The DTO containing fields such as minPrice, maxPrice, and avail to specify the filter parameters.
     *                       Each of these fields is optional, allowing for flexible filtering.
     * @return ResponseEntity containing the list of books that match the provided filter criteria. If no matches are found,
     *         an empty list is returned.
     */
    @PostMapping("/filter")
    public ResponseEntity<List<Book>> filterBooks(@RequestBody FilterBookDTO filterCriteria) {
        logger.info("Filtering for books using the following criteria:{}",filterCriteria);
        List<Book> searchedBooks = searchService.combinedFilter(filterCriteria);
        return ResponseEntity.ok(searchedBooks);

    }
}
