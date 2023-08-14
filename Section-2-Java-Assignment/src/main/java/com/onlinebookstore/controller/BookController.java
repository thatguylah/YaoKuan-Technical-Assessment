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
import io.swagger.annotations.*;


import java.util.Optional;
import java.util.List;

/**
 * A RESTful controller that manages and provides endpoints related to books.
 */
@Api(tags = "Book Management")  // <-- Class Level Annotation
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

    @ApiOperation(value = "Add a new book", notes = "Any 'id' value in the payload is ignored as it's supposed to be system-generated.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully added the book."),
            @ApiResponse(code = 400, message = "Invalid input data.")
    })
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

    @ApiOperation(value = "Retrieve a book by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the book."),
            @ApiResponse(code = 404, message = "Book not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> bookOpt = bookService.getBook(id);
        return bookOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Retrieve all books")
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @ApiOperation(value = "Remove a book by ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Successfully removed the book."),
            @ApiResponse(code = 404, message = "Book not found.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBook(@PathVariable Long id) {
        logger.info("Removing book with id: {}", id);
        bookService.removeBook(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Update a book's quantity")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated the book's quantity."),
            @ApiResponse(code = 404, message = "Book not found.")
    })
    @PutMapping("/{id}/quantity")
    public ResponseEntity<Book> updateBookQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        logger.info("Updating quantity for book id: {} with quantity: {}", id, quantity);
        Book updatedBook = bookService.updateBookQuantity(id, quantity);
        return ResponseEntity.ok(updatedBook);
    }

    @ApiOperation(value = "Retrieve a book's quantity")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the book's quantity."),
            @ApiResponse(code = 404, message = "Book not found.")
    })
    @GetMapping("/{id}/quantity")
    public ResponseEntity<Integer> getBookQuantity(@PathVariable Long id) {
        logger.info("Retrieving quantity for book id: {}", id);
        Integer quantity = bookService.getBookQuantity(id);
        return ResponseEntity.ok(quantity);
    }

    @ApiOperation(
            value = "Search for books by given criteria",
            notes = "The criteria can include fields such as title, author, and ISBN. " +
                    "The search operation uses fuzzy matching to provide results that closely match " +
                    "the provided criteria, even if they're not exact. For instance, if 'Har' is provided in the title, " +
                    "it may return books titled 'Harry Potter' or 'Harbour'. Each of these fields is optional, allowing for flexible searches."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the list of matching books."),
            @ApiResponse(code = 400, message = "Invalid input data.")
    })
    @PostMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestBody SearchBookDTO searchCriteria) {
        logger.info("Searching for books using the following criteria:{}",searchCriteria);
        List<Book> searchedBooks = searchService.combinedSearch(searchCriteria);
        return ResponseEntity.ok(searchedBooks);

    }

    @ApiOperation(
            value = "Filter books by given criteria",
            notes = "This method is designed to narrow down results by focusing on specific attributes such as price range and availability. " +
                    "The filter can be set to focus on a range of prices or filter out books that aren't available in stock. " +
                    "For example, specifying a minPrice and maxPrice will return books priced within that range. " +
                    "Specifying avail as true will return only those books that are currently in stock. Each of these fields is optional, allowing for flexible filtering."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the list of matching books."),
            @ApiResponse(code = 400, message = "Invalid input data.")
    })
    @PostMapping("/filter")
    public ResponseEntity<List<Book>> filterBooks(@RequestBody FilterBookDTO filterCriteria) {
        logger.info("Filtering for books using the following criteria:{}",filterCriteria);
        List<Book> searchedBooks = searchService.combinedFilter(filterCriteria);
        return ResponseEntity.ok(searchedBooks);

    }
}
