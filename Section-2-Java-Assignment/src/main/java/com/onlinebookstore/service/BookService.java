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
     * Save a new book to the database.
     *
     * @param book the book entity to be saved.
     * @return the saved book entity.
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
     * Remove a book from the database by its ID.
     *
     * @param id the ID of the book to be removed.
     */

    public void removeBook(Long id) {
        bookRepository.deleteById(id);
    }

    /**
     * Update the quantity of a specific book.
     *
     * @param id the ID of the book.
     * @param quantity the new quantity to set.
     * @return the updated book entity.
     * @throws IllegalArgumentException if the book with the given ID is not found.
     */
    public Book updateBookQuantity(Long id, Integer quantity) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setQuantity(quantity);
            return bookRepository.save(book);
        }

        throw new IllegalArgumentException("Book with the given ID not found.");
    }
    /**
     * Obtain the quantity of a specific book.
     *
     * @param id the ID of the book.
     * @return the quantityInStock of book entity.
     * @throws IllegalArgumentException if the book with the given ID is not found.
     */
    public Integer getBookQuantity(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            return bookOptional.get().getQuantity();
        }

        throw new IllegalArgumentException("Book with the given ID not found.");
    }
}
