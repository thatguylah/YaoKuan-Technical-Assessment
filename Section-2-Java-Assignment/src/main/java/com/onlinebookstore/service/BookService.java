package com.onlinebookstore.service;

import com.onlinebookstore.model.entity.Book;
import com.onlinebookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Persist a new book entity into the database.
     *
     * @param book the book entity to be persisted.
     * @return the persisted book entity.
     */
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Retrieve a book by its ID.
     *
     * @param id the ID of the book to be retrieved.
     * @return an Optional<Book> which contains the book if found. Possible the book ID no longer exists.
     */
    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Retrieve all books from the database.
     *
     * @return a list of all books.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Deletes a book from the database using its unique ID.
     * Throws an exception if the book does not exist.
     *
     * @param id the unique ID of the book to be deleted.
     * @throws BookNotFoundException if no book with the given ID is found.
     */

    public void removeBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }
    }

    /**
     * Updates the quantity of a book identified by its unique ID.
     *
     * @param id the unique ID of the book.
     * @param quantity the new quantity to be set for the book.
     * @return the updated book entity with the new quantity.
     * @throws BookNotFoundException if no book with the given ID is found.
     */
    public Book updateBookQuantity(Long id, Integer quantity) {
        return bookRepository.findById(id).map(book -> {
            book.setQuantity(quantity);
            return bookRepository.save(book);
        }).orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found."));
    }

    /**
     * Retrieves the stock quantity of a specific book using its unique ID.
     *
     * @param id the unique ID of the book.
     * @return the current stock quantity of the specified book.
     * @throws BookNotFoundException if no book with the given ID is found.
     */
    public Integer getBookQuantity(Long id) {
        return bookRepository.findById(id)
                .map(Book::getQuantity)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found."));
    }

    public static class BookNotFoundException extends RuntimeException {
        public BookNotFoundException(String message) {
            super(message);
        }
    }
}
